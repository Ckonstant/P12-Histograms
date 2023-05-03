import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class meros1o{

    public static void main(String[] args) {

        String mycsv = "acs2015_census_tract_data.csv"; // vazoume to arheio pou theloyme / path
        String line;
        String splitcsv = ","; // xorizoume ta stoiheia tou csv me komma
        ArrayList<Double> incomes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(mycsv))) {

            while ((line = br.readLine()) != null) {

                String[] token = line.split(splitcsv);

                if (!token[13].equals("") && token[13].matches("\\d+(\\.\\d+)?")) { // tsekaroume ta ta incomes einai arithmoi kai oxi null values
                    double income = Double.parseDouble(token[13]); // metatropi string se double
                    incomes.add(income);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.printf("%d valid income values \n" , incomes.size());

        int numBins = 100;
        double minIncome = Collections.min(incomes);
        double maxIncome = Collections.max(incomes);
        System.out.printf("minimum income = " + minIncome);
        System.out.printf(" maximum income = " + maxIncome);
        System.out.println();
        double binWidth = (maxIncome - minIncome) / numBins; //to mhkos tou kathe bin = 2461.39
        
        int [] histogramWidth = new int [numBins];
        	
        for (double income : incomes) {
            int binPoint = (int) Math.floor((income - minIncome) / binWidth);
            if (binPoint == numBins) { // an to income einai iso me to maxIncome tote ena piso.
            	binPoint--;
            }
            histogramWidth[binPoint]++;
        }
        
        // now we have the histogram data in an array
        System.out.println("equiwidth: ");
        for (int i = 0; i < numBins; i++) {
            double rangeStart = minIncome + i * binWidth; //to proto range tha einai to minincome kai paei legontas analoga me to mhkos tou kathe bin
            double rangeEnd = rangeStart + binWidth; //to deytero range that einai oso to rangeStart + to mhkos tou bin
            System.out.printf("range: [%.2f - %.2f) , ", rangeStart, rangeEnd); //kanoume print ta ranges
            
              System.out.print("numtuples: "+histogramWidth[i]); //kai epeita print tis pleiades
            
            System.out.println();
        }
        
        
        Collections.sort(incomes); //kanoume sort ta incomes gia na ftiaksoume ta isapehouses pleiades gia to DEPTH histogram

        System.out.println("equidepth: ");
        double range = maxIncome - minIncome;
        double binSize = range / 100.0;
        double[] histogramDepth = new double[100];
        int binPoints = incomes.size() / 100;
        for (int i = 0; i < 100; i++) {
            int startIndex = i * binPoints; //start index deihnei ston taksinomimeno incomes to proto steiheio dhladh to mikrotero
            int endIndex = (i + 1) * binPoints ; //dihnei sto amesos epomeno stoiheio tou pinaka

            if (i == 99) { // to teleytaio bin ehei ola ta points
                endIndex = incomes.size() - 1;
            }

            double rangeStart = incomes.get(startIndex); //to proto range tou bin
            double rangeEnd = incomes.get(endIndex); //to deytero range tou bin 

            for (int j = startIndex; j < endIndex; j++) {
                histogramDepth[i]++;
            }

            System.out.printf("range: [%.2f,%.2f), numtuples: %.0f\n", rangeStart, rangeEnd, histogramDepth[i]);
        }
        
        
    }

}
