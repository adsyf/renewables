package adsyf.renewables.demand.metering.providers.emporia;

import adsyf.renewables.demand.Circuit;
import adsyf.renewables.demand.Meter;
import adsyf.renewables.shared.PeriodUsage;
import adsyf.renewables.shared.TimeBlock;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import adsyf.renewables.shared.Usage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Data
public class CsvRawDataImport {
    private Meter virtMeter = new Meter();
    private HashMap<Integer, Circuit> circuits = new HashMap<>();
    private TimeZone timeZone;

    public static TimeZone getTimeZone(String c1Header){
        int tzNameStart = c1Header.indexOf("(") + 1;
        int tzNameEnd = c1Header.indexOf(")");
        String tzName = c1Header.substring(tzNameStart,tzNameEnd);
        log.info("got tzName {}",tzName);
        TimeZone tz = TimeZone.getTimeZone(tzName);
        return tz;
    }

    public void setUpMeters(List<String[]> fileContents){
        String[] header = fileContents.get(0);
        String[] firstRow = fileContents.get(1);
        this.timeZone = getTimeZone(header[0]);
        for (int i = 1; i < header.length; i++) {
            System.out.println(i + ": " + header[i]);
            int startCircuit = header[i].indexOf("-") + 1;
            String meterName = header[i].substring(0,startCircuit -1);
            log.info("got meterName {}",meterName);
            int startCirUnits = header[i].lastIndexOf("(") + 1;
            String cirUnits = header[i].substring(startCirUnits,header[i].length() - 1);
            log.info("got cirUnits {}",cirUnits);
            int startCirName = header[i].lastIndexOf("-") + 1;
            String cirName = header[i].substring(startCirName,startCirUnits -2);
            log.info("got cirName {}",cirName);
            String cirType="";
            log.info("startCircuit: {}, startCirName: {}",startCircuit,startCirName);

            if (startCirName - 1 > startCircuit) {
                cirType = header[i].substring(startCircuit, startCirName - 1);
                log.info("got cirType {}", cirType);
            }
            Circuit circuit = new Circuit();
            circuit.setName(cirName);
            circuit.setType(cirType);
            circuit.setPowerUsed(new HashMap<Integer,Usage>());
            circuits.put(i,circuit);
        }

    }

    public void hourlyReader(File hourlyCsv) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(hourlyCsv));
        List<String[]> contents = reader.readAll();
        setUpMeters(contents);
        log.info("read {}",hourlyCsv.getAbsolutePath());
        log.info("hourlyCsv has {} rows",contents.size());
        int rowCnt=0;
        int mainsACnt=0;
        int leapYearExtraHoursCnt=0;
        for (String[] row : contents){
            rowCnt++;
            if ( row[0].startsWith("Time Bucket") ) {
                log.debug("found header row ignore");
                continue;
            } else {
                log.debug("got time bucket {}",row[0]);
                ZoneId zId = timeZone.toZoneId();
                //https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/format/DateTimeFormatter.html
                DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm:ss");
                ZonedDateTime startTime = LocalDateTime.parse(row[0],formatter).atZone(zId);

                /*if (startTime.getMonth().equals(Month.FEBRUARY) && startTime.getDayOfMonth()==29){
                    log.info("ignoring leap year day..{}",startTime);
                    leapYearExtraHoursCnt++;
                } else {*/
                    TimeBlock timeBlock = new TimeBlock(startTime, ChronoUnit.HOURS);
                    Integer blockOfYear = timeBlock.getUnitOfYear();
                    for (int i = 1; i < row.length; i++) {
                        if (!row[i].equals("No CT")) {
                            Circuit c = circuits.get(i);
                            String kwhVal = row[i];
                            Usage dupUsage = c.getPowerUsed().put(blockOfYear, new Usage(timeBlock, new BigDecimal(kwhVal)));
                            if (dupUsage != null) {
                                log.info("dupUsage found for i {}, BoY {}, og dt {}, KWh {}, dup dt {}, KWh {} ",i,blockOfYear,dupUsage.getTimeblock().getStartTime(),dupUsage.getKWh(),timeBlock.getStartTime(),kwhVal);
                            }
                            if (c.getName().equals("Mains_A")) {
                                mainsACnt++;
                            }

                        }
                    }
                //}
            }
        }
        log.info("looped through {} rows, leapYearExtraHoursCnt {}",rowCnt,leapYearExtraHoursCnt);
        circuits.forEach((key,circuit) -> {
            if (circuit.getName().startsWith("Mains_A")){
                    log.info("Mains_A usages cnt {}",circuit.getPowerUsed().size());
                }
        });
        virtMeter.setName("default");
        circuits.forEach((key,circuit) -> {
            if (!circuit.getPowerUsed().isEmpty()) {
                log.info("found measured circuit that has pulled power: {}",circuit.getName());
                if (circuit.getName().startsWith("Mains_")){
                    virtMeter.setNextPhasePowerUsed(circuit.getPowerUsed());
                }
            }
        });
        if (virtMeter.getNoOfPhases().equals(1)){
            log.info("single phase supply assign all measured circuits that have pulled power");
            circuits.forEach((key,circuit) -> {
                if (!circuit.getPowerUsed().isEmpty()) {
                    if (! circuit.getName().startsWith("Mains_")){
                        virtMeter.getL1Circuits().add(circuit);
                    }
                }
            });
        } else {
            throw new UnsupportedOperationException("code cannot manage multi-phase data");
        }
        log.info("stop");
    }

    public static void main(String[] args) throws IOException, CsvException {
        File file = new File("C:\\Users\\adsyf\\Downloads\\data-export-208020-2246416567614596069\\58A778-Newmarket_Road-1H.csv");
        CsvRawDataImport csvRawDataImport = new CsvRawDataImport();
        csvRawDataImport.hourlyReader(file);
        PeriodUsage mainsA = new PeriodUsage(csvRawDataImport.virtMeter.getL1powerUsed(),ChronoUnit.HOURS);
        System.out.println("printing details for MainsA");
        mainsA.print(Boolean.FALSE);
        BigDecimal allCircuitsTotal = new BigDecimal(0);
        for (Circuit circuit: csvRawDataImport.virtMeter.getL1Circuits()){
            PeriodUsage p = new PeriodUsage(circuit.getPowerUsed(), ChronoUnit.HOURS);
            allCircuitsTotal = allCircuitsTotal.add(p.getTotalKWh());
            System.out.println("printing details for: "+ circuit.getName());
            p.print();
        }
        System.out.println("total for all circuits: "+ allCircuitsTotal);
        //TimeZone tz = CsvRawDataImport.getTimeZone("Time Bucket (Europe/London)");
        log.info("stop");
    }

}
