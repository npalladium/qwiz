####### Multi-Stage Build #######

FROM node:latest as build
WORKDIR /usr/local/app
COPY ./ /usr/local/app/
COPY etc/nginx/ng.conf /etc/nginx/conf.d/
RUN rm /etc/nginx/conf.d/default.conf
RUN npm install && npm run build

FROM nginx:latest
COPY --from=build /usr/local/app/dist/qwiz-frontend /usr/share/nginx/html
EXPOSE 80
