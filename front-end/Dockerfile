### STAGE 1: Build ###
FROM node:alpine as builder

COPY package.json package-lock.json ./

RUN npm ci && mkdir /ng-app && mv ./node_modules ./ng-app
WORKDIR /ng-app

COPY . .

RUN npm run ng build -- --configuration production --output-path=dist


### STAGE 2: Setup ###
FROM nginx:stable-alpine

COPY --from=builder /ng-app/default.conf /etc/nginx/conf.d/default.conf

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /ng-app/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
