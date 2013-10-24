int BlueLED = 13;
int WhiteLED = 11;
int RedLED = 12;
int Pushbutton1 = 0;
int iter_0;

void setup() {
  pinMode(BlueLED, OUTPUT);  
  pinMode(WhiteLED, OUTPUT);  
  pinMode(RedLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 if (!digitalRead(Pushbutton1))
 {
  for (iter_0=0; iter_0 < ( 5 ); ++iter_0 )
  {
   digitalWrite(BlueLED, (HIGH));
   digitalWrite(WhiteLED, (LOW));
   digitalWrite(RedLED, (LOW));
   delay(200);
   digitalWrite(BlueLED, (LOW));
   digitalWrite(WhiteLED, (HIGH));
   delay(200);
   digitalWrite(WhiteLED, (LOW));
   digitalWrite(RedLED, (HIGH));
   delay(200);
   digitalWrite(BlueLED, (LOW));
   digitalWrite(WhiteLED, (LOW));
   digitalWrite(RedLED, (LOW));
   delay(200);
  }
 }
}
