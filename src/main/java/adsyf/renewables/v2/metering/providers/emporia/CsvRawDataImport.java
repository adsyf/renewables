package adsyf.renewables.v2.metering.providers.emporia;

import adsyf.renewables.v2.Circuit;
import adsyf.renewables.v2.Usage;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import adsyf.renewables.v2.Period;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Data
public class CsvRawDataImport {
    private TimeZone timeZone;

    private static TimeZone getTimeZone(String c1Header){
        int tzNameStart = c1Header.indexOf("(") + 1;
        int tzNameEnd = c1Header.indexOf(")");
        String tzName = c1Header.substring(tzNameStart,tzNameEnd);
        log.info("got tzName {}",tzName);
        TimeZone tz = TimeZone.getTimeZone(tzName);
        return tz;
    }

    private void setCircuits(List<String[]> fileContents,Map<Integer, Circuit> circuits){
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
            circuits.put(i,circuit);
        }

    }

    public void populateCircuitsWithHourlyUsage(File hourlyCsv,Map<Integer, Circuit> circuits) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(hourlyCsv));
        List<String[]> contents = reader.readAll();
        setCircuits(contents,circuits);
        Circuit circuitsTotal = new Circuit();
        circuitsTotal.setName("total");
        circuitsTotal.setMeterTotal(true);
        log.info("read {}",hourlyCsv.getAbsolutePath());
        log.info("hourlyCsv has {} rows",contents.size());
        int rowCnt=0;
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
                Period period = new Period(startTime,startTime.plusHours(1));
                if (startTime.getMonth().equals(Month.FEBRUARY) && startTime.getDayOfMonth()==29){
                    log.info("ignoring leap year day..{}",startTime);
                    leapYearExtraHoursCnt++;
                } else {
                    for (int i = 1; i < row.length; i++) {
                        if (!row[i].equals("No CT")) {
                            Circuit c = circuits.get(i);
                            String kwhVal = row[i];
                            Usage usage = new Usage(period,new BigDecimal(kwhVal));
                            c.getAcKwh().add(usage);

                            if (c.getName().equals("Mains_A") || c.getName().equals("Mains_B") || c.getName().equals("Mains_C")){
                                log.debug("building total with circuit {}",c.getName());
                                Usage totalUsage;
                                if (circuitsTotal.getAcKwh().contains(usage)){
                                    totalUsage = circuitsTotal.getAcKwh().ceiling(usage);
                                    totalUsage.setKwh(totalUsage.getKwh().add(usage.getKwh()));
                                } else {
                                    totalUsage = new Usage(period,usage.getKwh());
                                    circuitsTotal.getAcKwh().add(totalUsage);
                                }
                            }
                        }
                    }
                }
            }
        }
        circuits.put(0,circuitsTotal);
        log.info("looped through {} rows, leapYearExtraHoursCnt {}",rowCnt,leapYearExtraHoursCnt);
        log.info("stop");
    }


    public static void main(String[] args) throws IOException, CsvException {
        Map<Integer, Circuit> circuits = new HashMap<>();
        File file = new File("C:\\Users\\adsyf\\Downloads\\data-export-208020-8380958110011221080\\58A778-Newmarket_Road-1H.csv");
        CsvRawDataImport importer = new CsvRawDataImport();
        importer.populateCircuitsWithHourlyUsage(file,circuits);

        log.info("finished");

    }

}
