import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class meros2o{

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
        
        
        
        
        while(true) {
        System.out.println("\nGive the range of [a,b) or give 0 if you want to terminate." );	    	
        System.out.print("Enter value for a range: " );
        Scanner sca = new Scanner(System.in);
        double a = sca.nextDouble();
        if (a == 0 )
        {
        	System.out.println("Program Terminated.");
        	break;
        }
        
        System.out.print("Enter value for b range: " );
        Scanner scb = new Scanner(System.in);
        double b = scb.nextDouble();
              
        int actualCount = 0;
        for (int i = 0; i < incomes.size(); i++) {
           if (incomes.get(i) >= a && incomes.get(i) < b) {
           actualCount++;
               }
           }
        
        System.out.println();
        System.out.printf("For a = %.1f and b = %.1f we get the following results.\n",a,b);
        estimateWidth(incomes,histogramWidth,a,b,binWidth);
        estimateDepth(incomes,histogramDepth,a,b,binPoints);
        System.out.println("actual results: "+actualCount);
        
        }
        
        
    }
    public static void estimateDepth(List<Double> incomeData, double[] histogram, double alpha, double beta, int binPoints) {

    double countTuples = 0; //metritis tuples gia kathe timi pou peftei sto range
    
    for (int i = 0; i < 100; i++) {
        int startIndex =   i * binPoints;
        int endIndex = (i + 1) * binPoints ;

        if (i == 99) { // to teleytaio bin ehei ola ta points
            endIndex = incomeData.size() - 1;
        }

        double rangeStart = incomeData.get(startIndex);
        double rangeEnd = incomeData.get(endIndex);

        if (rangeStart >= beta || rangeEnd < alpha) { // to bin peftei ekso apo to [a,b) opote to skiparoume 
            continue;
        } else {
            double diff = Math.min(beta, rangeEnd) - Math.max(alpha, rangeStart); //an peftoume sto range [a,b) tote kratame tin diafora gia na paroume to pososto.
            double percentage = diff / (rangeEnd - rangeStart); //ftiahnoume to pososto kai diairoume me ta tuples pou vriskonte mesa sto bin pou eimaste.
            countTuples += histogram[i] * percentage; //ston athroisti ton tuples prosthetoume pollaplasiasmeno me to pososto ta tuples.
        }

    }

    System.out.printf("equidepth estimated results: %.12f\n", countTuples); //printaroume ta apotelesmata
}
    
    public static void estimateWidth(List<Double> incomeData, int[] histogram, double alpha, double beta, double binWidth) {
    
    double countTuples = 0;
    
   
        for (int i = 0; i < 100; i++) {
            double rangeStart = 2611 + i * binWidth; //2611 vazoume gia minIncome.
            double rangeEnd = rangeStart + binWidth;
            if (rangeStart >= beta || rangeEnd <= alpha) { 
                continue;
            }
            double diff = Math.min(beta, rangeEnd) - Math.max(alpha, rangeStart);
            double percentage = diff / binWidth;
            countTuples += histogram[i] * percentage;
        }

    System.out.printf("equiwidth estimated results: %.12f\n", countTuples);
}

}
