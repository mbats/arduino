int RedLED = 11;
int BlueLED = 12;
int iter_1;
int iter_3;
int iter_5;

void setup() {
  pinMode(RedLED, OUTPUT);  
  pinMode(BlueLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(RedLED, (false));
 for (iter_1=0; iter_1 < ( 50 ); ++iter_1 )
 {
  digitalWrite(BlueLED, (true));
  delay(50);
  digitalWrite(BlueLED, (false));
  delay(50);
 }
 digitalWrite(BlueLED, (false));
 for (iter_5=0; iter_5 < ( 50 ); ++iter_5 )
 {
  digitalWrite(RedLED, (true));
  delay(50);
  digitalWrite(RedLED, (false));
  delay(50);
 }
 for (iter_3=0; iter_3 < ( 50 ); ++iter_3 )
 {
  digitalWrite(BlueLED, (true));
  digitalWrite(RedLED, (true));
  delay(50);
  digitalWrite(BlueLED, (false));
  digitalWrite(RedLED, (false));
  delay(50);
 }
}
