# Merchant’s Guide to Galaxy

### Problem Definition:
[Check problem definition in PROBLEM.MD](PROBLEM.MD)

One of the requirements is not to use frameworks at all (DI, ORM, MVC, etc.)

### How to run:
Use ./tools/scripts/run.sh to start the application.

For postgres storage:
- Run ./tools/scripts/run-db.sh to start postgres with pgadmin and portainers.
- Put postgres-<ENV>.properties with db configuration into resources.

## Todo:
[ ] Architectural Tests
[ ] Containerization
[ ] API Documentation
[ ] Monitoring

## Logging:
FILE and STDOUT Appenders are available to use (default: STDOUT).
File logs are stored inside build/logs directory.
Set LOG_APPENDER env variable to select.

## Profiles:
Create application-<ENV>.properties with ui and storage configuration.
For example, 
Configuration is optional; app will be created with CLI and InMemory storage by default.
Set APP_PROFILE env variable to select.

## Construction Logs:
- [Setup and configuration](./tools/adr/ADR-001.md)
- [Analysis](./tools/adr/ADR-002.md)
- [CLI module](./tools/adr/ADR-003.md)
- [Domain Factory Method convention](./tools/adr/ADR-004.md)
- [Application construction and Dependency Injection](./tools/adr/ADR-005.md)
