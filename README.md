# Immowealth

Immowealth is a project build to monitor your real estate investments
with a simple easy to use UI. The application provides forecasts, simple analysis 
and a central data storage for all your real estate projects.

There are some more advanced features planned in the future, so this application will
grow very fast and continously and will always have its users in focus. 

# Features

- Real estate overview
- Credit rate auto booking
- value forecast
- Inflation based value calculations
- Real estate map


# Installation

This application itself is written in Kotlin (Quarkus) and Typescript (Next.js) but uses Docker for its hosting.
Therefore the process of installation is quite easy. 

Clone the repository:
```shell
git clone https://github.com/MathisBurger/immowealth.git
```

Execute the docker compose:
```shell
docker-compose up -d
```


# Roadmap

There are many new features planned. You can find a little roadmap of planned future releases here:

### v1.2.0
- [x] Add translations
- [x] Add different currencies
- [x] Add activity logging
- [x] Save settings like filter selections
- [x] Split rent into credit rate and other expenses

### v1.3.0
- [ ] AutoPayInterval (configure start date)
- [ ] Add mail notifications
- [ ] Archiving feature
- [ ] Add document upload for important documents
- [ ] Most / recently used document recommendations

### v1.4.0
- [ ] Full UNIT test coverage
- [ ] User account system
- [ ] Support Chat for renter
- [ ] Rental status / statistics about the renter
- [ ] Performance calculations

### v1.5.0
- [ ] Immo portal search
- [ ] Object location watchlist
- [ ] Add support for measured object prices
- [ ] Tabs are saved in URI

### Not planned yet
- [ ] Auto collect market value change data
- [ ] Add balance sheet calculation
- [ ] Rent spread suggestions by fixes rules
- [ ] Rentability calculations
- [ ] TAX calculations

