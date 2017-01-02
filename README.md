## Application Purpose and Description

While developing my app [DCM](https://play.google.com/store/apps/details?id=jagerfield.dcm) I used both the ContentResolver and the CursorLoader to fetch contacts from the mobile. The ContentResolver is a class that provides applications access to the content model. The CursorLoader is a loader object that queries a ContentResolver for data. In my trials I noticed that the ContentResolver is much faster. 

This application compares the execution time of both methods in fetching contacts. The ContactsLib modle in this application is modified and extended from my ContactImportLib which solely used the ContentResolver to fetch contacts:

https://github.com/Jagerfield/Android-get-phone-book-contact-library

The following images are results of a test performed on the simulator. Real life tests on my Samsung tablet S2 showed that the ContentResolver was almost twice faster than the CursorLoader.

<img src="https://github.com/Jagerfield/Content-Resolver-Vs-Cursor-Loader-Speed-Test/blob/master/msc/Content%20Resolver.png" width="240">


<img src="https://github.com/Jagerfield/Content-Resolver-Vs-Cursor-Loader-Speed-Test/blob/master/msc/Cursor%20Loader.png" width="240">

## Installation

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
	   compile 'com.github.Jagerfield:Content-Resolver-Vs-Cursor-Loader-Speed-Test:v1.0'
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
