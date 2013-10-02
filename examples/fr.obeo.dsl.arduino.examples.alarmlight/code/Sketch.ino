int RedLED = 13;
int BlueLED = 12;
int iter_1;
int iter_2;
int iter_4;

void setup() {
  pinMode(RedLED, OUTPUT);  
  pinMode(BlueLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(RedLED, false);
 for (iter_2=0; iter_2 < ( 1 ); ++iter_2 )
 {
 }
 digitalWrite(BlueLED, false);
 for (iter_1=0; iter_1 < ( 2 ); ++iter_1 )
 {
 }
 for (iter_4=0; iter_4 < ( 3 ); ++iter_4 )
 {
 }
}
