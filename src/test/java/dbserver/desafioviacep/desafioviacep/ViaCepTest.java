package dbserver.desafioviacep.desafioviacep;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import dbserver.desafioviacep.desafioviacep.config.BaseTest;
import dbserver.desafioviacep.desafioviacep.models.Endereco;
import dbserver.desafioviacep.desafioviacep.stubs.EnderecoStubs;

class ViaCepTest extends BaseTest {

  @DisplayName("CT001.001 - Consulta um CEP válido")
  @Test
  void DeveRetornarStatusCode200EUmCepValido() {

    Endereco enderecoEsperado = EnderecoStubs.enderecoValidoEsperadoStub();

    given()
        .when()
        .get("/{cep}/json/", "01001-000")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("EnderecoEsperadoSchema.json"))
        .body("cep", equalTo(enderecoEsperado.getCep()))
        .body("logradouro", equalTo(enderecoEsperado.getLogradouro()))
        .body("complemento", equalTo(enderecoEsperado.getComplemento()))
        .body("bairro", equalTo(enderecoEsperado.getBairro()))
        .body("localidade", equalTo(enderecoEsperado.getLocalidade()))
        .body("uf", equalTo(enderecoEsperado.getUf()))
        .body("ibge", equalTo(enderecoEsperado.getIbge()))
        .body("gia", equalTo(enderecoEsperado.getGia()))
        .body("ddd", equalTo(enderecoEsperado.getDdd()))
        .body("siafi", equalTo(enderecoEsperado.getSiafi()));
  }

  @DisplayName("CT001.002 - Consulta um CEP inválido")
  @ParameterizedTest
  @CsvSource({
      "01001-0000",
      "45005_154",
      "310809703482748327"
  })
  void DeveRetornarStatusCode400EUmaMensagemBadRequest(String cep) {
    given()
        .when()
        .get("/{cep}/json/", cep)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString("Http 400"))
        .body(containsString("Verifique a URL"))
        .body(containsString("{Bad Request}"));
  }

  @DisplayName("CT001.003 - Consulta um CEP válido, porém inexistente")
  @Test
  void DeveRetornarStatusCode200ETrueQuandoCepInexistente() {
    given()
        .when()
        .get("/{cep}/json/", "99999-999")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("CepValidoPoremInexistenteSchema.json"))
        .body("erro", equalTo(true));
  }

  @DisplayName("CT001.004 - Consulta sem CEP")
  @Test
  void DeveRetornarStatusCode400EUmaMensagemBadRequestQuandoConsultarSemCep() {
    given()
        .when()
        .get("/{cep}/json/", "")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString("Http 400"))
        .body(containsString("Verifique a URL"))
        .body(containsString("Bad Request"));
  }

  @DisplayName("CT002.001 - Consulta de CEP por endereço com sucesso")
  @Test
  void DeveRetornarStatusCode200ECEPCorrespondenteQuandoEnderecoValido() {
    Endereco enderecoEsperado = EnderecoStubs.enderecoValidoEsperadoStub();

    given()
        .pathParam("uf", enderecoEsperado.getUf())
        .pathParam("cidade", enderecoEsperado.getLocalidade())
        .pathParam("logradouro", enderecoEsperado.getLogradouro())
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("CepsSchema.json"))
        .body("$", hasSize(5))
        .body("cep", hasItems(enderecoEsperado.getCep()))
        .body("logradouro", hasItems(enderecoEsperado.getLogradouro()))
        .body("complemento", hasItems(enderecoEsperado.getComplemento()))
        .body("bairro", hasItems(enderecoEsperado.getBairro()))
        .body("localidade", hasItems(enderecoEsperado.getLocalidade()))
        .body("uf", hasItems(enderecoEsperado.getUf()))
        .body("ibge", hasItems(enderecoEsperado.getIbge()))
        .body("gia", hasItems(enderecoEsperado.getGia()))
        .body("ddd", hasItems(enderecoEsperado.getDdd()))
        .body("siafi", hasItems(enderecoEsperado.getSiafi()));
  }

  @DisplayName("CT002.002 - Consulta de CEP por endereço com UF, cidade e logradouro inválidos")
  @Test
  void DeveRetornarStatusCode400QuandoUfCidadeLogradouroInvalidos() {
    given()
        .pathParam("uf", "X X")
        .pathParam("cidade", "Terra do nunca")
        .pathParam("logradouro", "rua imaginaria")
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString("Http 400"))
        .body(containsString("Verifique a URL"))
        .body(containsString("Bad Request"));
  }

  @DisplayName("CT002.003 - Consulta de CEP por endereço com UF vazia")
  @Test
  void DeveRetornarStatusCode400QuandoUfVazia() {
    given()
        .pathParam("uf", "")
        .pathParam("cidade", "São Paulo")
        .pathParam("logradouro", "Praça da Sé")
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString("Http 400"))
        .body(containsString("Verifique a URL"))
        .body(containsString("Bad Request"));
  }

  @DisplayName("CT002.004 - Consulta de CEP por endereço com cidade vazia")
  @Test
  void DeveRetornarStatusCode400QuandoCidadeVazia() {
    given()
        .pathParam("uf", "SP")
        .pathParam("cidade", "")
        .pathParam("logradouro", "Praça da Sé")
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString("Http 400"))
        .body(containsString("Verifique a URL"))
        .body(containsString("Bad Request"));
  }

  @DisplayName("CT002.005 - Consulta de CEP por endereço com logradouro vazio")
  @Test
  void DeveRetornarStatusCode400QuandoLogradouroVazio() {
    given()
        .pathParam("uf", "SP")
        .pathParam("cidade", "São Paulo")
        .pathParam("logradouro", "")
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString("Http 400"))
        .body(containsString("Verifique a URL"))
        .body(containsString("Bad Request"));
  }

  @DisplayName("CT002.006 - Consulta de CEP por endereço com UF inválida")
  @ParameterizedTest
  @CsvSource({
      "R S, 8orto Alegre, Domingos",
      "$P, sp, Paulista",
      "B4, $alvador, Santana"
  })
  void DeveRetornarStatusCode400QuandoUFInvalida(String uf, String cidade, String logradouro) {
    given()
        .pathParam("uf", uf)
        .pathParam("cidade", cidade)
        .pathParam("logradouro", logradouro)
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString("Http 400"))
        .body(containsString("Verifique a URL"))
        .body(containsString("Bad Request"));
  }

  @DisplayName("CT002.007 - Consulta de CEP por endereço com cidade inválida")
  @ParameterizedTest
  @CsvSource({
      "RS, 8, Domingos",
      "SP, sp, Paulista",
      "BA, $alvador, Santana"
  })
  void DeveRetornarStatusCode400QuandoCidadeInvalida(String uf, String cidade, String logradouro) {
    given()
        .pathParam("uf", uf)
        .pathParam("cidade", cidade)
        .pathParam("logradouro", logradouro)
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString("Http 400"))
        .body(containsString("Verifique a URL"))
        .body(containsString("Bad Request"));
  }

  @DisplayName("CT002.008 - Consulta de CEP por endereço com logradouro inválido")
  @ParameterizedTest
  @CsvSource({
      "RS, Porto Alegre, 1",
      "SP, Sao Paulo, _Paulista",
      "BA, Salvador, $antana"
  })
  void DeveRetornarStatusCode400QuandoLogradouroInvalido(String uf, String cidade, String logradouro) {
    given()
        .pathParam("uf", uf)
        .pathParam("cidade", cidade)
        .pathParam("logradouro", logradouro)
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString("Http 400"))
        .body(containsString("Verifique a URL"))
        .body(containsString("Bad Request"));
  }

  @DisplayName("CT002.009 - Consulta de CEP com cidade válida, porém inexistente no Estado")
  @Test
  void DeveRetornarStatusCode200EArrayVazioQuandoCidadeValidaPoremInexistenteNoEstado() {
    given()
        .pathParam("uf", "RS")
        .pathParam("cidade", "São Gonçalo")
        .pathParam("logradouro", "Domingos")
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("ArrayVazioSchema.json"))
        .body(equalTo("[]"))
        .body("$", hasSize(0));
  }

  @DisplayName("CT002.010 - Consulta de CEP com logradouro válido, porém inexistente no Estado")
  @Test
  void DeveRetornarStatusCode200EArrayVazioQuandoLogradouroValidaPoremInexistenteNaCidade() {
    given()
        .pathParam("uf", "RS")
        .pathParam("cidade", "Porto Alegre")
        .pathParam("logradouro", "logradouro")
        .when()
        .get("/{uf}/{cidade}/{logradouro}/json/")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("ArrayVazioSchema.json"))
        .body(equalTo("[]"))
        .body("$", hasSize(0));
  }
}
