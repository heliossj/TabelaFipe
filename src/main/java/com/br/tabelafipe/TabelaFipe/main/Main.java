package com.br.tabelafipe.TabelaFipe.main;
import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
import com.br.tabelafipe.TabelaFipe.models.Data;
import com.br.tabelafipe.TabelaFipe.models.Models;
import com.br.tabelafipe.TabelaFipe.service.ConsumoApi;
import com.br.tabelafipe.TabelaFipe.service.ConverteDados;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    Scanner reader = new Scanner(System.in);
    private final String URL_BASE = "https://fipe.parallelum.com.br/api/v2/";
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu(){
        String menu = """
                *** OPÇÕES ***
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consultar:
                """;
        System.out.println();
        System.out.println(menu);
        var option = reader.nextLine();

        String address;
        if (option.toLowerCase().contains("car")){
            address = URL_BASE + "cars/brands";
        } else if (option.toLowerCase().contains("mot")){
            address = URL_BASE + "motorcycles/brands";
        } else {
            address = URL_BASE + "trucks/brands";
        }
        var json = consumo.obterDados(address);
        //System.out.println(json);

        var brands = conversor.obterLista(json, Data.class);
        brands.stream()
                .sorted(Comparator.comparing(Data::code))
                .forEach(System.out::println);

        System.out.println("\nInforme o código da marca para consulta:");
        var codeBrand = reader.nextLine();

        address += "/" + codeBrand + "/models";

        json = consumo.obterDados(address);

        var listModel = conversor.obterLista(json, Data.class);
        listModel.stream()
                .sorted(Comparator.comparing(Data::code))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do carro a ser buscado");
        var vehicleName = reader.nextLine();

        List<Data> filteredModels = listModel.stream()
                .filter(n -> n.name().toLowerCase().contains(vehicleName.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados:");
        filteredModels.forEach(System.out::println);

        //Problem na API para execução do código abaixo
        /*
        System.out.println("\nDigite o código do modelo:");
        var modelCode = reader.nextLine();

        address += "/" + modelCode + "years";
        json = consumo.obterDados(address);

        List<Data> years = conversor.obterLista(json, Data.class);

        */

    }
}
