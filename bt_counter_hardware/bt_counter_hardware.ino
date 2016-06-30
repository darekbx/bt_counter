const int refreshDelay = 5;
const int counterValue = 1;
const int cadenceValue = 2;

int counterPin = 3;
int cadencePin = 4;
int stateLed = 5;

boolean isCounterHigh = false;
boolean isCadenceHigh = false;

void setup() {
  Serial.begin(115200);
  
  pinMode(counterPin, INPUT);
  pinMode(cadencePin, INPUT);
  pinMode(stateLed, OUTPUT);
}

void loop() {
  handleCounter();
  handleCadence();
  delay(refreshDelay);
}

void handleCounter() {
  int counterButtonState = digitalRead(counterPin);
  if (counterButtonState == HIGH && !isCounterHigh) {
    isCounterHigh = true;
    Serial.write(counterValue);
    digitalWrite(stateLed, HIGH);
  } else if (isCounterHigh && counterButtonState == LOW) {
    isCounterHigh = false;
    digitalWrite(stateLed, LOW);
  }
}

void handleCadence() {
  int cadenceButtonState = digitalRead(cadencePin);
  if (cadenceButtonState == HIGH && !isCadenceHigh) { 
    isCadenceHigh = true; 
    Serial.write(cadenceValue);
    digitalWrite(stateLed, HIGH);
  } else if (isCadenceHigh && cadenceButtonState == LOW) { 
    isCadenceHigh = false;
    digitalWrite(stateLed, LOW);  
  }
}
