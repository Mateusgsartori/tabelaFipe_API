package br.com.tabelaFipe.main;

import br.com.tabelaFipe.model.Dados;
import br.com.tabelaFipe.model.Modelos;
import br.com.tabelaFipe.model.Veiculo;
import br.com.tabelaFipe.service.ConsumoApi;
import br.com.tabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi api = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        System.out.println("***OPÇÕES***\n");
        System.out.println("- Carros");
        System.out.println("- Motos");
        System.out.println("- Caminhões");

        System.out.println("Digite um dos tipos do veículo:");
        String tipoVeiculo = leitura.nextLine();
        String endereco = "https://parallelum.com.br/fipe/api/v1/";
        String endercoTipoVeiculo = endereco + tipoVeiculo.toLowerCase().replace("õ", "o") + "/marcas";

        var json = api.obterDados(endercoTipoVeiculo);
        List<Dados> dados = conversor.obterLista(json, Dados.class);

        dados.stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);

        System.out.println("Digite o código da marca:");
        var codigoMarca = leitura.nextLine();

        String enderecoModelo = endercoTipoVeiculo + "/" + codigoMarca + "/modelos";
        json = api.obterDados(enderecoModelo);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca:");
        modeloLista.modelos().stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);

        System.out.println("\nDigite um trecho de um dos modelos");
        var trechoModelo = leitura.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream().filter(m -> m.nome().toLowerCase().contains(trechoModelo.toLowerCase())).toList();

        System.out.println("\nModelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("\nDigite o código do carro que deseja buscar: ");
        var codigoVeiculo = leitura.nextLine();

        String enderecoCodigoVeiculo = enderecoModelo + "/" + codigoVeiculo + "/anos";

        json = api.obterDados(enderecoCodigoVeiculo);
        dados = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (Dados dado: dados) {
            String ednerecoAno = enderecoCodigoVeiculo + "/" + dado.codigo();
            json = api.obterDados(ednerecoAno);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos filtrados com avaliação por ano");
        veiculos.forEach(System.out::println);







    }

}
