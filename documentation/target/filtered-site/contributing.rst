Contributing
============

Documentation
-------------
Feel free to send a pull request with any fixes / updates / improvement to our docs that you think might help.

Feature Requests
----------------
Create a post in the `google group <https://groups.google.com/d/forum/seekay-contracts>`_ with as much detail as you can give, sample contracts would be awesome too.

Bugs
----
Create an issue on the project `github <https://github.com/harmingcola/contract/issues>`_ with as much detail as possible, contracts / test cases that recreate the bug would be awesome. Even better, a pull request that fixes the bug.

Merge Requests
--------------
1. Fork & clone the project
2. Implement whatever it is that you'd like
3. Push to a branch in your forked repo with a name that roughly explains what you're implementing
4. Update docs where necessary.
5. Create a pull request into the master branch on our repo
6. If it passes our build process and analysis we'll accept.


Our Design Considerations / Conventions / Quirks
------------------------------------------------
1. The main project and two testing projects are written in Java
2. Unit tests are written in `Spock <http://spockframework.github.io/spock/docs/1.0/index.html>`_
3. Documentation written in `Sphinx <http://www.sphinx-doc.org/en/stable/>`_
4. We lean towards an expressive and clean code style, if you've ever read Clean Code by Bob Martin, that is what we aspire to.
5. Any added features must be tested via the kvClient & kvServer applications.