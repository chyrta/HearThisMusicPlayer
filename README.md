# HearThisAt Music Player

An extremely simple player of popular tracks from HearThisAt, utilizing Model-View-Intent pattern and a modified DMAudioStreamer.

## Screenshots

<p>
  <img src="art/screenshot1.png" width="25%">
  <img src="art/screenshot2.png" width="25%">
  <img src="art/screenshot3.png" width="25%">
</p>


## Architecture

This project utilizes MVI pattern according to the official sample by [oldergod](https://github.com/oldergod/android-architecture)

## Unit tests

To run unit tests, use this command:

```
./gradlew test
```

## Linting

To perform linting of the code:

```
./gradlew ktlint
```

Also, there is another command to fix the code automatically:

```
./gradlew ktlintFormat
```

## Libraries

* Modified DMAudioStreamer
* RxJava 2
* Retrofit
* Koin
* Timber
* Glide
* Moshi
* RxBinding
