package dbserver.desafioviacep.desafioviacep;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import dbserver.desafioviacep.desafioviacep.config.BaseTest;
import dbserver.desafioviacep.desafioviacep.models.Endereco;
import dbserver.desafioviacep.desafioviacep.models.Menssagem;
import dbserver.desafioviacep.desafioviacep.stubs.EnderecoStubs;
import dbserver.desafioviacep.desafioviacep.stubs.MenssagensStubs;

public class ConsultaCepTest extends BaseTest {

  private Menssagem menssagemDeErro;
  public static final String BASE_URL_CEP = "/{cep}/json/";

  @DisplayName("CT001.001 - Consulta um CEP válido")
  @Test
  public void DeveRetornarStatusCode200EUmCepValido() {

    Endereco enderecoEsperado = EnderecoStubs.enderecoValidoEsperadoStub();

    Endereco enderecoObtido = given()
        .when()
        .get(BASE_URL_CEP, "01001-000")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .body()
        .as(Endereco.class);

    assertThat(enderecoObtido.getCep(), equalTo(enderecoEsperado.getCep()));
    assertThat(enderecoObtido.getLogradouro(), equalTo(enderecoEsperado.getLogradouro()));
    assertThat(enderecoObtido.getComplemento(), equalTo(enderecoEsperado.getComplemento()));
    assertThat(enderecoObtido.getBairro(), equalTo(enderecoEsperado.getBairro()));
    assertThat(enderecoObtido.getLocalidade(), equalTo(enderecoEsperado.getLocalidade()));
    assertThat(enderecoObtido.getUf(), equalTo(enderecoEsperado.getUf()));
    assertThat(enderecoObtido.getIbge(), equalTo(enderecoEsperado.getIbge()));
    assertThat(enderecoObtido.getGia(), equalTo(enderecoEsperado.getGia()));
    assertThat(enderecoObtido.getDdd(), equalTo(enderecoEsperado.getDdd()));
    assertThat(enderecoObtido.getSiafi(), equalTo(enderecoEsperado.getSiafi()));
  }

  @DisplayName("CT001.002 - Consulta um CEP inválido")
  @ParameterizedTest
  @CsvSource({
      "01001-0000",
      "45005_154",
      "310809703482748327"
  })
  public void DeveRetornarStatusCode400EUmaMensagemBadRequest(String cep) {

    menssagemDeErro = MenssagensStubs.menssagemDeErroStub();

    given()
        .when()
        .get(BASE_URL_CEP, cep)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString(menssagemDeErro.getStatus()))
        .body(containsString(menssagemDeErro.getTitle()))
        .body(containsString(menssagemDeErro.getDescricao()));
  }

  @DisplayName("CT001.003 - Consulta um CEP válido, porém inexistente")
  @ParameterizedTest
  @CsvSource({
      "99999-999",
      "12345678",
      "54321-987"
  })
  public void DeveRetornarStatusCode200ETrueQuandoCepInexistente(String cep) {
    given()
        .when()
        .get(BASE_URL_CEP, cep)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("erro", equalTo(true));
  }

  @DisplayName("CT001.004 - Consulta sem CEP")
  @Test
  public void DeveRetornarStatusCode400EUmaMensagemBadRequestQuandoConsultarSemCep() {

    menssagemDeErro = MenssagensStubs.menssagemDeErroStub();

    given()
        .when()
        .get(BASE_URL_CEP, "")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString(menssagemDeErro.getStatus()))
        .body(containsString(menssagemDeErro.getTitle()))
        .body(containsString(menssagemDeErro.getDescricao()));
  }
}
