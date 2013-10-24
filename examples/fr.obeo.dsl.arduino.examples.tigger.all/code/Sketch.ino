#include <Servo.h>
#include <Servo.h>
int BlueLED = 13;
int WhiteLED = 11;
int RedLED = 12;
Servo Microservo1;
int Pushbutton1 = 0;
int Infraredsensor = 4;
int iter_0;

void setup() {
  pinMode(BlueLED, OUTPUT);  
  pinMode(WhiteLED, OUTPUT);  
  pinMode(RedLED, OUTPUT);  
 Microservo1.attach(9);
}

// the loop routine runs over and over again forever:
void loop() {
 if (!digitalRead(Pushbutton1))
 {
  for (iter_0=0; iter_0 < ( 5 ); ++iter_0 )
  {
   digitalWrite(BlueLED, (!HIGH));
   digitalWrite(WhiteLED, (!LOW));
   digitalWrite(RedLED, (!LOW));
   delay(200);
   digitalWrite(BlueLED, (!LOW));
   digitalWrite(WhiteLED, (!HIGH));
   delay(200);
   digitalWrite(WhiteLED, (!LOW));
   digitalWrite(RedLED, (!HIGH));
   delay(200);
   digitalWrite(BlueLED, (!LOW));
   digitalWrite(WhiteLED, (!LOW));
   digitalWrite(RedLED, (!LOW));
   delay(200);
  }
 }
 if (!digitalRead(Infraredsensor))
 {
  Microservo1.write(90);
  delay(400);
  Microservo1.write(0);
  delay(400);
 }
}
