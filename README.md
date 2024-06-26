# Immowealth
[![](https://tokei.rs/b1/github/MathisBurger/immowealth?category=lines)](https://github.com/XAMPPRocky/tokei).

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
- Multiple currencies
- Rent expenses
- Document storage
- Archive


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

### v1.4.0
- [x] Full UNIT test coverage (services)
- [x] User account system
- [ ] Support Chat for renter
- [ ] Rental status / statistics about the renter
- [ ] Update mail notifications with favourites
- [ ] Performance calculations (by input of data)

### v1.4.1 (after stable React 19 release)
- [ ] Upgrade to a Next version that supports React compiler

### v1.5.0 (not scheduled yet)
- [ ] Full UNIT test coverage (all classes, doesn`t matter how simple they are => more complex edge-cases)
- [ ] Subtenants
- [ ] Immo portal search
- [ ] Object location watchlist
- [ ] Add support for measured object prices
- [ ] Tabs are saved in URI

### v1.5.3 (not scheduled yet)
- [ ] Backend translations
- [ ] 2FA
- [ ] Rent spread suggestions by fixes rules
- [ ] Validation of emails and other fields with strict rules
- [ ] Data backup functionality

### v1.6.0 (not scheduled yet)
- [ ] Mail templates to send
- [ ] Rentability calculations
- [ ] Auto collect market value change data

### Not planned yet
- [ ] Add balance sheet calculation
- [ ] New password rule validation
- [ ] Translation test coverage
- [ ] Add code quality linting with Prettier and eslint and also for kotlin
- [ ] Login with LDAP
- [ ] Assign chat to specific people
- [ ] Profile picture upload
- [ ] TAX calculations
- [ ] Extended chat functionality
- [ ] Configure Chat Socket keep alive

## Local setup

1. Clone repo
```shell
git clone https://github.com/MathisBurger/immowealth.git
```
2. Generate keys

```shell
openssl genrsa -out rsaPrivateKey.pem 2048
openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem
openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem
```
3. Setup your POstgreSQL DB
4. Build the code and start developing

