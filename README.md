

## ContentResolver Vs CursorLoader Speed Test

While developing my app [DCM](https://play.google.com/store/apps/details?id=jagerfield.dcm) I used both the ContentResolver and the CursorLoader to fetch contacts from the mobile. The ContentResolver is a class that provides applications access to the content model. The CursorLoader is a loader object that queries a ContentResolver for data. In my trials I noticed that the ContentResolver is much faster. 

This application compares the execution time of both methods in fetching contacts. The library is modified and extended from my [Mobile-Contacts-Library](https://github.com/Jagerfield/Mobile-Contacts-Library) which solely used the ContentResolver to fetch contacts:

The following images are results of a test performed on the simulator. Real life tests on my Samsung tablet S2 showed that the ContentResolver was almost twice faster than the CursorLoader.

<img src="https://github.com/Jagerfield/Resolver-Vs-Loader/blob/master/msc/ContentResolver.png" width="240"> &#160;<img src="https://github.com/Jagerfield/Resolver-Vs-Loader/blob/master/msc/Cursor%20Loader.png" width="240">

The demo app for this library is available on Google Play:

<a href='https://play.google.com/store/apps/details?id=jagerfield.ContentResolverVsCursorLoader'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="150" height="60"/></a>

## Installation

In the app manifest file add this permission: 

  ```
  <uses-permission android:name="android.permission.READ_CONTACTS" />
  ``` 
  
In the app build.gradle add the following:

  a. Add JitPack repository at the end of repositories 

  ```java
  repositories 
  {
     maven { url 'https://jitpack.io' }
  }
 
  ```
  b. Add the dependency
 
  ```java
  dependencies 
  {
	   compile 'com.github.Jagerfield:Resolver-Vs-Loader:v1.3'
  }
  
  ```

## How to use?
You can use this library to fetch contacts using either one of the methods or both:

### Fetch contacts using ContentResolver

```java
  new ImportContacts(getActivity(), new ImportContacts.ContentResolverCallback() 
  {
      @Override
      public void getMobileContacts(ArrayList<Contact> contacts)
       {
           if (contacts != null)
             {
                //Write your code here
             }
        }
  });
  
```

### Fetch contacts using CursorLoader

```java
  new ImportContacts(getActivity(), new ImportContacts.CursorLoaderCallback() 
  {
      @Override
      public void getMobileContacts(ArrayList<Contact> contacts) 
       {
           if (contacts != null)
             {
                //Write your code here
             }
        }
  });

```
