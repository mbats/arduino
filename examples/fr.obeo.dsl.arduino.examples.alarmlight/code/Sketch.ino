int RedLED = 12;
int BlueLED = 13;
int iter_1;
int iter_3;
int iter_5;

void setup() {
  pinMode(RedLED, OUTPUT);  
  pinMode(BlueLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(RedLED, (LOW));
 for (iter_1=0; iter_1 < ( 25 ); ++iter_1 )
 {
  digitalWrite(BlueLED, (LOW));
  delay(100);
  digitalWrite(BlueLED, (HIGH));
  delay(100);
 }
 digitalWrite(BlueLED, (LOW));
 for (iter_5=0; iter_5 < ( 25 ); ++iter_5 )
 {
  digitalWrite(RedLED, (HIGH));
  delay(100);
  digitalWrite(RedLED, (LOW));
  delay(100);
 }
 for (iter_3=0; iter_3 < ( 25 ); ++iter_3 )
 {
  digitalWrite(BlueLED, (LOW));
  digitalWrite(RedLED, (LOW));
  delay(100);
  digitalWrite(BlueLED, (HIGH));
  digitalWrite(RedLED, (HIGH));
  delay(100);
 }
 digitalWrite(BlueLED, (LOW));
}
