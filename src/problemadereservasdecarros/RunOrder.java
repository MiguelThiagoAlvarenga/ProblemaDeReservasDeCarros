/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemadereservasdecarros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sun.security.util.Length;

/**
 *
 * @author Doutriem Pro
 */
class RunOrder {
    
     private static boolean successfully = false;
     private static String file          = "";

    /**
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean Run (String path) throws FileNotFoundException, IOException {
        int numLine      = 0;       
        BufferedReader br = new BufferedReader(new FileReader(path));
                
        while(br.ready()){
            String line = br.readLine();
            if (numLine > 0){
                CheckCarCheaperPerPerson(line, path);
            }
            numLine++;
        }
        
        WriteToFile(file);
        br.close();
        return successfully;
    }
    
    private static void CheckCarCheaperPerPerson(String line, String path) throws IOException{
        int numLine      = 0;
        String fileParc  = "";
        //Dados do cliente --- Data e ocupantes.
        List<String> data         = new ArrayList();
        double qtd        = 0;
        double custoTotal = 0;
        
        //Dados da loja.
        String nomeLoja  = "";
        String tipoCarro = "";
        String carroDisp = "";
        double precoDiasUteis           = 0;
        double precoDiasUteisFidelidade = 0;
        double precoFimSemana           = 0;
        double precoFimSemanaFidelidade = 0;
        double qtdOcupantes             = 0;

        BufferedReader br1 = new BufferedReader(new FileReader("./Dados/INPUT_SHOPS.txt"));
        
        int posInicial = 0;
        //Pegando os valores da linha de entrada, ou seja, dos clientes.
        for (int i = 0; i < line.length(); i++){
            String sub = line.substring(i, i+1);
            if (sub.equalsIgnoreCase(":")){
                if (posInicial == 0 ) {
                    qtd = Double.parseDouble(line.substring(i+1, i+2));
                    posInicial = i;
                } else {
                    data.add(line.substring(i+1, i+15));
                }    
            }else if (sub.equalsIgnoreCase(",")){
                data.add(line.substring(i+1, i+15));  
            }        
        }

        //Pegando valores das lojas.        
        while(br1.ready()){
            double custoTotalParcial = 0;
            String lineShops = br1.readLine();
            if (numLine > 0){
                nomeLoja  = lineShops.substring(0, 8);
                tipoCarro = lineShops.substring(9, 19);
                carroDisp = lineShops.substring(113, 128);
                precoDiasUteis           = Double.parseDouble(lineShops.substring(20, 34));
                precoDiasUteisFidelidade = Double.parseDouble(lineShops.substring(35, 59));
                precoFimSemana           = Double.parseDouble(lineShops.substring(60, 74));
                precoFimSemanaFidelidade = Double.parseDouble(lineShops.substring(75, 99));
                qtdOcupantes             = Double.parseDouble(lineShops.substring(100, 112));   

                if (data.size() > 0) {
                    if (qtd < qtdOcupantes + 1){
                        //Problema pede custo cliente normal, calculo encima do mesmo.
                        for (int i = 0; i <  data.size()-1; i++){
                            String day = data.get(i).substring(10, 13);
                            if (day.equalsIgnoreCase("Sub" ) || day.equalsIgnoreCase("Dom")) {
                                custoTotalParcial = custoTotalParcial + precoFimSemana;
                            } else {
                                custoTotalParcial = custoTotalParcial + precoDiasUteis;
                            }
                        }
                        if (custoTotal == 0){
                            custoTotal = custoTotalParcial;
                            fileParc =  carroDisp.trim() + ":" + nomeLoja.trim() + "\n";
                        } else if (custoTotal > custoTotalParcial){
                            custoTotal = custoTotalParcial;                    
                            fileParc =  carroDisp.trim() + ":" + nomeLoja.trim() + "\n";
                        }
                    } 
                }                            
            }
            numLine++;
        }     
        
        if (!fileParc.isEmpty()){
           file = file + fileParc;    
        }

        successfully = true;
    }
    
    public static void WriteToFile(String file) throws IOException {        
        try {
            BufferedWriter buffWrite = new BufferedWriter(new FileWriter("./Dados/OUTPUT.txt"));
            buffWrite.append(file);
            
            buffWrite.close();     
        } catch (IOException e) {
            throw new RuntimeException("Algum erro ocorre ao gravar o arquivo de saída! \n Verifique o arquivo OUTPUT.TXT \n ERRO:" + e);
        }
    }    
}
