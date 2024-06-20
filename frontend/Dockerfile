FROM node:20

WORKDIR /usr/src/app
COPY . /usr/src/app

RUN npm install -g @angular/cli
RUN npm install
RUN export NG_CLI_ANALYTICS=off off
CMD ["ng", "serve", "--host", "0.0.0.0"]
