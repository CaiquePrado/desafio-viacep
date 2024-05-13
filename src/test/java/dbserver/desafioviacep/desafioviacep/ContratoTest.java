package dbserver.desafioviacep.desafioviacep;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dbserver.desafioviacep.desafioviacep.config.BaseTest;
import dbserver.desafioviacep.desafioviacep.models.Endereco;
import dbserver.desafioviacep.desafioviacep.stubs.EnderecoStubs;

public class ContratoTest extends BaseTest {

  public static final String BASE_URL_ENDERECO = "/{uf}/{cidade}/{logradouro}/json/";

  @DisplayName("Teste de contrato Cep válido")
  @Test
  public void DeveValidarCepValidoContrato() {
    when()
        .get(ConsultaCepTest.BASE_URL_CEP, "01001-000")
        .then().statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("EnderecoEsperadoSchema.json"));
  }

  @DisplayName("Teste de contrato Cep válido, porém inexistente")
  @Test
  public void DeveValidarCepValidoInexistenteContrato() {
    when()
        .get(ConsultaCepTest.BASE_URL_CEP, "99999-999")
        .then().statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("CepValidoPoremInexistenteSchema.json"));
  }

  @DisplayName("Teste de contrato consulta de cep por endereço")
  @Test
  public void DeveValidarConsultaDeCepPorEnderecoValidoContrato() {
    Endereco enderecoEsperado = EnderecoStubs.enderecoValidoEsperadoStub();

    given()
        .pathParam("uf", enderecoEsperado.getUf())
        .pathParam("cidade", enderecoEsperado.getLocalidade())
        .pathParam("logradouro", enderecoEsperado.getLogradouro())
        .when()
        .get(BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("CepsSchema.json"));
  }

  @DisplayName("Teste de contrato consulta de cep por endereço")
  @Test
  public void DeveValidarConsultaDeCepPorEnderecoComCodadeExistenteinvalidaContrato() {

    Endereco enderecoEsperado = EnderecoStubs.cidadeExistenteInvalidaStub();

    given()
        .pathParam("uf", enderecoEsperado.getUf())
        .pathParam("cidade", enderecoEsperado.getLocalidade())
        .pathParam("logradouro", enderecoEsperado.getLogradouro())
        .when()
        .get(BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("ArrayVazioSchema.json"));
  }

  @DisplayName("Teste de contrato consulta de cep por endereço")
  @Test
  public void DeveValidarConsultaDeCepPorEnderecoComLogradouroExistenteinvalidaContrato() {

    Endereco enderecoEsperado = EnderecoStubs.logradouroExistenteInvalidaStub();

    given()
        .pathParam("uf", enderecoEsperado.getUf())
        .pathParam("cidade", enderecoEsperado.getLocalidade())
        .pathParam("logradouro", enderecoEsperado.getLogradouro())
        .when()
        .get(BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("ArrayVazioSchema.json"));
  }
}
