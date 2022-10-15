// MyAIDLInterface.aidl
package com.example.ch15_outer;

// Declare any non-default types here with import statements
//AIDL은 외부앱과 연동이 목적
interface MyAIDLInterface {
    int getMaxDuration();
    void start();
    void stop();
}