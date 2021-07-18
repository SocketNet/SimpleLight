#include <ESP8266WiFi.h>
#include <ESPAsyncTCP.h>
#include <ESPAsyncWebServer.h>

AsyncWebServer server(80);

const char *ssid = "Redmi_58F1";
const char *password = "xiaog123";

int ledPin = D0;

void setup()
{

  Serial.begin(9200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, HIGH);

  if (WiFi.waitForConnectResult() != WL_CONNECTED)
  {
    Serial.printf("WiFi Failed!\n");
    return;
  }

  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());

  server.on("/turn", HTTP_GET, [](AsyncWebServerRequest *request)
            {
              digitalRead(ledPin) ? digitalWrite(ledPin, LOW) : digitalWrite(ledPin, HIGH);
              request->send(200, "text/plain", "");
            });

  server.on("/state", HTTP_GET, [](AsyncWebServerRequest *request)
            { digitalRead(ledPin) ? request->send(200, "text/plain", "off") : request->send(200, "text/plain", "on"); });

  server.begin();
}

void loop()
{
}