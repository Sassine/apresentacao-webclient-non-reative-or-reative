# Chega de RestTemplate, Conheça o WebClient

A nova abordagem recomendada pelo Spring para fazer chamadas de API de forma reativa ou bloqueante.

- Um projeto simples para o tutorial de como utilizar o WebClient do Spring 5 em aplicações reativas/ não bloquetantes e bloqueantes


| Requisitos para executar | versão  |
|--|--|
| Maven | **3.6+** |
| JDK|  **8+** ( no pom altere o "<java.version>18</java.version>" para sua versão )|

Como utilizar

inicie sua aplicação a partir da sua IDE ou a partir da linha de comando 
`$ mvn spring-boot:run`

Abra o seu navegado preferido e acesse **localhost:8080/cep**
Você terá dois links de acesso rápido se quiser para testar, mas se preferir pode digitar na sua barra de endereço substituindo o numero "17015311" pelo seu CEP ✨😄

```json
{
  "Normal": "/cep/17015311",
  "Reativo": "/cep/non-block/17015311"
}
```

Lei mais em [blog.sassine.dev - Post completo](https://blog.sassine.dev//chega-de-resttemplate-conheca-o-webclient)