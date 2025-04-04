# App Doação de Sangue - Garra da Vida (Desenvolvimento)

## Descrição
O **Garra da Vida** é um aplicativo mobile projetado para facilitar e incentivar a doação de sangue. Ele permite que os usuários realizem agendamentos, recebam lembretes e informações sobre doação de sangue, e tenham acesso a um cartão digital de doador.

## Tecnologias Utilizadas
- **Linguagem:** Java + XML
- **Banco de Dados:** Firebase
- **IDE:** Android Studio
- **Gerenciador de Dependências:** Gradle
- **Versão Mínima do Android:** Nougat 7.0

## Funcionalidades
- **Cadastro e Login**: Permite que os usuários se cadastrem e acessem o aplicativo.
- **Agendamento de Doação**: Facilita o agendamento de doações de sangue em bancos de sangue parceiros.
- **Lembretes**: Uma tela  para lembrar o usuário sobre doações futuras e também sendo possível desmarcar.
- **Avisos e Informações**: Exibe informações relevantes sobre doação de sangue.
- **Tela de Conscientização**: Fornece dados e estatísticas para incentivar a doação.
- **Cartão do Doador** (*em desenvolvimento*): Funcionalidade para o usuário armazenar seu histórico de doações.
- **Clube Doador** (*em desenvolvimento*): Um sistema de pontuação para parcerias com prefeituras ou instituições, permitindo que doadores resgatem recompensas na "loja" do app.

## Imagens do App

<p align="center">
  <img src="https://github.com/user-attachments/assets/eea2b836-4f86-4f83-a8d0-a167adca1978" width="200"/>
  <img src="https://github.com/user-attachments/assets/82bfe6b4-ac36-4ad4-b988-d87c4670000f" width="199"/>
  <img src="https://github.com/user-attachments/assets/33fb99fb-ee54-455e-b6a5-60c068e64d65" width="194"/>
  <img src="https://github.com/user-attachments/assets/b8bb99ba-6d65-4ac5-8c34-2400667e94c0" width="200"/>
  <img src="https://github.com/user-attachments/assets/367d7ec9-dc43-4070-8232-baeb9a50cdae" width="200"/>
</p>

## Como Executar o Projeto
1. Clone este repositório:
   ```bash
   git clone https://github.com/LCS-Simoes/AppDoacao.git
   ```
2. Abra o projeto no Android Studio.
3. Configure o Firebase no seu projeto:
   - Adicione o arquivo `google-services.json` na pasta `app`.
   - Configure o Firebase Authentication e o Realtime Database/Firestore.
4. Compile e execute o app em um emulador ou dispositivo físico.

## Como Contribuir
1. Faça um fork do repositório.
2. Crie uma nova branch para sua feature ou correção:
   ```bash
   git checkout -b minha-feature
   ```
3. Faça as alterações necessárias e commit:
   ```bash
   git commit -m "Adicionando nova funcionalidade"
   ```
4. Envie suas alterações:
   ```bash
   git push origin minha-feature
   ```
5. Abra um Pull Request no repositório principal.



