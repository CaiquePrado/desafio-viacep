package dbserver.desafioviacep.desafioviacep.stubs;

import dbserver.desafioviacep.desafioviacep.models.Endereco;

public interface EnderecoStubs {

  public static Endereco enderecoValidoEsperadoStub() {
    return Endereco.builder()
        .cep("01001-000")
        .logradouro("Praça da Sé")
        .complemento("lado ímpar")
        .bairro("Sé")
        .localidade("São Paulo")
        .uf("SP")
        .ibge("3550308")
        .gia("1004")
        .ddd("11")
        .siafi("7107")
        .build();
  }

  public static Endereco cidadeExistenteInvalidaStub() {
    return Endereco.builder()
        .logradouro("Domingos")
        .localidade("São Gonçalo")
        .uf("RS")
        .build();
  }

  public static Endereco logradouroExistenteInvalidaStub() {
    return Endereco.builder()
        .logradouro("Frei Benjamin")
        .localidade("Porto Alegre")
        .uf("RS")
        .build();
  }
}
