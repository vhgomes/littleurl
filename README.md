# Encurtador de Link


Este é um simples encurtador de links que permite aos usuários encurtarem URLs longas em URLs mais curtas, facilitando o compartilhamento em redes sociais, mensagens e outros meios digitais. Este projeto foi desenvolvido em Spring Boot.


## Funcionalidades
- **Registro e autenticação**: Registro de usuários e autenticação por JWT.
- **ROLES**: roles para a administração dos links (usuário comum e administrador).
- **Encurtamento de Links**: Transforme URLs longas em URLs curtas.
- **Rastreamento de Estatísticas**: Acompanhe o número de cliques em cada link encurtado.

## Rotas URLS
- **GET** "/urls": retorna todas as rotas registradas pelo o usuário, ela recebe um token JWT como parâmetro.
- **POST** "/urls": responsável pela criação das urls encurtadas. Recebe um token JWT e uma string da url que deseja ser encurtada.

## Rotas Users
- **GET** "/register": responsável pela a criação de usuários.
- **POST** "/login": recebe o username e password e retorna um token JWT.

## Recursos Futuros
-  [ ] Personalização de links
-  [ ] Verificação de links maliciosos
-  [ ] Criação de links que sejam expirados
-  [ ] Implementar uma categorização dos links

## Contato

Se você tiver alguma dúvida, sugestão ou problema, sinta-se à vontade a criar uma PR.



