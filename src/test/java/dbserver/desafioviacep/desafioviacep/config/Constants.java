package dbserver.desafioviacep.desafioviacep.config;

import io.restassured.http.ContentType;

public interface Constants {

  final String APP_BASE_URL = "https://viacep.com.br";
  final String APP_BASE_PATH = "/ws"; 

  final ContentType APP_CONTENT_TYPE = ContentType.JSON;

  final Long MAX_TIME_OUT = 2000L;
}