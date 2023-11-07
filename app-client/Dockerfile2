FROM nginx:1.25.3

WORKDIR /usr/share/nginx/html

# 이전 빌드 단계에서 빌드한 결과물을 /usr/share/nginx/html 으로 복사한다.
RUN rm -rf ./*
COPY dist /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
# custom 설정파일을 컨테이너 내부로 복사한다.
COPY nginx.conf /etc/nginx/conf.d/nginx.conf

# 컨테이너의 80번 포트를 열어준다.
EXPOSE 80

# nginx 서버를 실행하고 백그라운드로 동작하도록 한다.
CMD ["nginx", "-g", "daemon off;"]
