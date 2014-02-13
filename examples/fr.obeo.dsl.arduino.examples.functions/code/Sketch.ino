int BlueLED = 13;
int RedLED = 11;
int iter_8;
int iter_10;

void setup() {
  pinMode(BlueLED, OUTPUT);  
  pinMode(RedLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 for (iter_8=0; iter_8 < ( 5 ); ++iter_8 )
 {
  // LEDBlinker({param1:delay(100);},{param2:digitalWrite(BlueLED, (!HIGH));},{param3:digitalWrite(BlueLED, (!LOW));})
  delay(100);
  digitalWrite(BlueLED, (!HIGH));
  delay(100);
  digitalWrite(BlueLED, (!LOW));
 }
 delay(3000);
 for (iter_10=0; iter_10 < ( 5 ); ++iter_10 )
 {
  // LEDBlinker({param1:delay(300);},{param2:digitalWrite(RedLED, (!HIGH));},{param3:digitalWrite(RedLED, (!LOW));})
  delay(300);
  digitalWrite(RedLED, (!HIGH));
  delay(300);
  digitalWrite(RedLED, (!LOW));
 }
}
