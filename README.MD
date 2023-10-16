# Merchant’s Guide to Galaxy

### Problem Definition:
[Check problem definition in PROBLEM.MD](PROBLEM.MD)

### How to run:
Use ./tools/scripts/run.sh to start the application.

### How to set up:
Run ./tools/scripts/install-hooks.sh

## Technical description:
- Test runner and assertions library: Kotest

- Static code analyzer: Detekt
- Linter: Ktlint
- Code coverage analyzer: Jacoco

## How I did it:
#### Before
DX is one of the most important things to me, so I started with configuring reliable and friendly environment:
   - Linter, static and coverage analyzer to have consistent codebase and some basic numbers from reports.
   - Scripts and hooks will save me some time on remembering the right Gradle command.
   - And finally safe test scope to encourage writing tests and make it as easy as possible.

I hate to be distracted on such things in the heat of the work.
(I've done it 3 times now, maybe I need some kind of starter script)
