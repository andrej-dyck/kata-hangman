<img src="https://raw.githubusercontent.com/yegor256/hangman/master/images/logo.png" width="100px"/>

[![Build Status](https://travis-ci.com/andrej-dyck/kata-hangman.svg?branch=master)](https://travis-ci.com/github/andrej-dyck/kata-hangman)

[Hangman](https://en.wikipedia.org/wiki/Hangman_%28game%29) is a words
guessing game for one player. The computer guesses a word and the user
has to suggest letters one by one. Every time the word contains a letter,
the computer opens it (all of them, if there are a few). Every time the
word doesn't contain a letter, the computer gives a penalty point for
the user. If there are five penalty points, the user looses. If there
are no hidden letters anymore, the computer looses.

Build it:

```
$ ./gradlew build
```

Test it:

```
$ ./gradlew check
```

Run it:

```
$ ./gradlew run
```

## It is not object-oriented

This Java code is very procedural, imperative and not elegant at all.

Please, try to refactor it and improve. You're welcome to create new classes,
new methods, anything. Just make sure it still builds.

When ready, please submit a pull request.

## License

The MIT License (MIT)

Copyright (c) 2017 Yegor Bugayenko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.
