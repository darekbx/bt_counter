const int refreshDelay = 1;
const int cadenceValue = 2;
const int counterPin = 3;
const int cadencePin = 4;
const int stateLed = 5;

boolean isCounterHigh = false;
boolean isCadenceHigh = false;

long lastCounterTime = 0;

void setup() {
  Serial.begin(115200);
  
  pinMode(counterPin, INPUT);
  pinMode(cadencePin, INPUT);
  pinMode(stateLed, OUTPUT);
  
  lastCounterTime = millis();
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
    unsigned long diff = calculateDiff();
    writeBytes(diff);
    digitalWrite(stateLed, HIGH);
  } else if (isCounterHigh && counterButtonState == LOW) {
    isCounterHigh = false;
    digitalWrite(stateLed, LOW);
  }
}

void writeBytes(long value) {
  byte buf[4];
  buf[0] = value & 255;
  buf[1] = (value >> 8)  & 255;
  buf[2] = (value >> 16) & 255;
  buf[3] = (value >> 24) & 255;
  Serial.write(buf, sizeof(buf));
}

long calculateDiff() {
  long currentTime = millis();
  long diff = currentTime - lastCounterTime;
  lastCounterTime = currentTime;
  return diff;
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
