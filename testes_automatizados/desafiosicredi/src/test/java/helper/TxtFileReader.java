package helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TxtFileReader {
	
	public List<String> lerArquivo(String nomeArquivo) {
        String path = "src/test/resources/dataResources/{0}.txt";
        path = MessageFormat.format(path, nomeArquivo);
		List<String> linhas = new ArrayList<>();
        
        try {
            File arquivo = new File(path);
            Scanner scanner = new Scanner(arquivo);
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                linhas.add(linha);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + e.getMessage());
        }
        return linhas;
    }
}
