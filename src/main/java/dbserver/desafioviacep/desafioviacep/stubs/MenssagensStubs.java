package dbserver.desafioviacep.desafioviacep.stubs;

import dbserver.desafioviacep.desafioviacep.models.Menssagem;

public interface MenssagensStubs {

  public static Menssagem menssagemDeErroStub() {
    return Menssagem.builder()
        .status("Http 400")
        .title("Verifique a URL")
        .descricao("{Bad Request}")
        .build();
  }
}