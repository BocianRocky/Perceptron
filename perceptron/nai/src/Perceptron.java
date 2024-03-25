import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Perceptron {
    public static void main(String[] args) {
        String file ="Iris_training.txt";    //first file
        String secfile ="Iris_test.txt";     //second file
        ArrayList<String[]> data = przechwytywanie(file);
        double theta=0;
        double alpha=0.3;
        int lineLength=data.get(0).length;
        double []weightVector = new double[lineLength]; //weight Vector
        Arrays.fill(weightVector,0.4);
        weightVector[lineLength-1]=-1;
        double []score = new double[data.size()];

        for (int i = 0; i < data.size(); i++) {
            if(data.get(i)[lineLength-1].equals("Iris-setosa")){
                score[i]=1.0;
            }
            else
                score[i]=0.0;
        }
        boolean flag = true;
        while(flag) {
            flag=false;
            for (int i = 0; i < data.size(); i++) {
                double[] inputVec = new double[lineLength];
                for (int j = 0; j < inputVec.length - 1; j++) {
                    inputVec[j]=Double.parseDouble(data.get(i)[j]);
                }
                inputVec[lineLength-1]=theta;
                double y=OutputPerceptron(weightVector,inputVec);
                if (score[i]-y!=0.0) {
                    flag=true;
                    boolean secflag=true;
                    while(secflag) {
                        for (int j = 0; j < weightVector.length; j++) {
                            weightVector[j]+=(score[i]-y)*alpha*inputVec[j];
                        }
                        double u=OutputPerceptron(weightVector,inputVec);
                        if (score[i]-u==0.0){
                            secflag=false;
                        }
                    }
                }



            }
        }
//        for (int i = 0; i < weightVector.length; i++) {
//            System.out.print(weightVector[i]+" ");
//        }
//        System.out.println();
////////////////////////////////////////////////////
        checkTest(lineLength,secfile,weightVector,theta);
        inputParameters(lineLength,theta,weightVector);


    }


    public static ArrayList<String[]> przechwytywanie(String file) {
        ArrayList<String[]> data = new ArrayList<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace(",", ".");
                line = line.replaceFirst("^\\s+", "");
                String[] atr = line.split("\\s+");
                data.add(atr);
            }
            fr.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static void inputParameters(int lineLength, double theta, double []weightVector){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("\npodaj parametry ["+(lineLength-1)+"]: ");
            double[] parameters = new double[lineLength];
            for (int i = 0; i < parameters.length - 1; i++) {
                parameters[i] = scanner.nextDouble();
                if (parameters[i] == -111) {
                    throw new RuntimeException("bledne parametry");
                }
            }
            parameters[lineLength - 1] = theta;
            double ilskal = 0;
            for (int i = 0; i < parameters.length; i++) {
                ilskal += parameters[i] * weightVector[i];
            }
            System.out.print("type: ");
            System.out.println(ilskal>=0?"Iris-setosa":"Iris-nie-setosa");
        }

    }

    public static void checkTest(int lineLength, String secfile, double []weightVector, double theta){
        ArrayList<String[]> checkdata = przechwytywanie(secfile);
        double []checkscore = new double[checkdata.size()];
        for (int i = 0; i < checkdata.size(); i++) {
            if(checkdata.get(i)[lineLength-1].equals("Iris-setosa")){
                checkscore[i]=1.0;
            }
            else
                checkscore[i]=0.0;
        }
        int count=0;
        double x=0;
        for (int i = 0; i < checkdata.size(); i++) {
            for (int j = 0; j < checkdata.get(i).length-1; j++) {
                x+=weightVector[j]*Double.parseDouble(checkdata.get(i)[j]);
            }
            x+=weightVector[checkdata.get(i).length-1]=theta;
            x=(x>=0)?1.0:0.0;
            if(checkscore[i]-x==0){
                count++;
            }
        }
        System.out.println("Prawidlowo zaklasyfikowane przyklady: "+count+"/"+checkdata.size());
        System.out.println("Dokladnosc eksperymentu: "+((double)count/checkdata.size())*100+"%");
    }
    public static double OutputPerceptron(double []weightVector, double []inputVec){
        double y=0.0;
        for (int j = 0; j < weightVector.length; j++) {
            y+=inputVec[j]*weightVector[j];
        }
        y=(y>=0)?1.0:0.0;
        return y;
    }
}


