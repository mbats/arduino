#include <Servo.h>
#include <Servo.h>
int BlueLED = 13;
int Fan = 7;
Servo Microservo2;
int Soundsensor = 1;
int iter_0;

void setup() {
  pinMode(BlueLED, OUTPUT);  
  pinMode(Fan, OUTPUT);  
 Microservo2.attach(8);
}

// the loop routine runs over and over again forever:
void loop() {
 if (analogRead(Soundsensor)>128)
 {
  digitalWrite(BlueLED, (!HIGH));
  for (iter_0=0; iter_0 < ( 3 ); ++iter_0 )
  {
   Microservo2.write(0);
   delay(50);
   digitalWrite(Fan, (!HIGH));
   delay(1500);
   digitalWrite(Fan, (!LOW));
   Microservo2.write(60);
   delay(1500);
  }
 }
 digitalWrite(BlueLED, (!LOW));
}
