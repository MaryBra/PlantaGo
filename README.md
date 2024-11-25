Por: Caio Willian Runpfe e Mariana Marques Braguim.

PlantaGo 🌱
Bem-vindo ao PlantaGo, um aplicativo Android desenvolvido para ajudar você a gerenciar suas plantas de forma simples e intuitiva. Com o PlantaGo, você pode cadastrar suas plantas, adicionar fotos, registrar históricos de rega e manter um acompanhamento detalhado de cada uma delas.

Sobre o Projeto
O PlantaGo foi criado com o objetivo de facilitar o cuidado com plantas, oferecendo uma interface amigável e recursos úteis para amantes de jardinagem, sejam eles iniciantes ou experientes.

Funcionalidades
Cadastro de Plantas: Adicione novas plantas ao seu catálogo pessoal, incluindo nome, descrição, categoria e foto opcional.
Edição de Plantas: Atualize as informações das suas plantas a qualquer momento.
Histórico de Regas: Registre cada vez que você regar uma planta, mantendo um histórico organizado por data e hora.
Exclusão de Plantas: Remova plantas que você não possui mais ou não deseja acompanhar.
Navegação Intuitiva: Utilize uma interface simples com navegação fácil entre as telas principais, detalhes, cadastro e edição.
Tecnologias Utilizadas
Kotlin: Linguagem de programação principal do aplicativo.
Jetpack Compose: Biblioteca moderna para construção de interfaces de usuário declarativas.
Room Database: Biblioteca de persistência para armazenamento local de dados.
Coroutines: Para operações assíncronas sem bloquear a interface do usuário.
Coil: Para carregamento eficiente de imagens.
Estrutura do Projeto
View: Contém as telas e componentes de interface do usuário.
Model: Define as classes de dados, como Planta e Historico.
Dao: Interfaces para acesso ao banco de dados, como PlantaDao e HistoricoDao.
ViewModel: Gerencia a lógica de negócios e o estado das telas, seguindo o padrão MVVM.
Database: Configuração do banco de dados Room (AppDatabase).
Como Executar o Projeto
Pré-requisitos:

Android Studio instalado (recomendado a versão mais recente).
Ambiente configurado para desenvolvimento Android com SDK atualizado.
Clonar o Repositório:

bash
Copiar código
git clone https://github.com/seu-usuario/planta-go.git
Abrir no Android Studio:

Abra o Android Studio.
Selecione "Open an Existing Project" e navegue até a pasta clonada.
Sincronizar o Projeto:

Aguarde o Android Studio sincronizar as dependências (Gradle Sync).
Executar o Aplicativo:

Conecte um dispositivo Android ou inicie um emulador.
Clique no botão "Run" (ou use o atalho Shift + F10).
Selecione o dispositivo de destino e aguarde a instalação.
Uso do Aplicativo
Tela Inicial:

Ao abrir o aplicativo, você verá a tela principal com a lista de suas plantas.
Se não houver plantas cadastradas, uma mensagem será exibida.
Adicionar Nova Planta:

Clique no botão flutuante "+" para acessar a tela de cadastro.
Preencha os campos obrigatórios: nome, descrição e categoria.
Opcionalmente, adicione uma foto tirando uma nova ou selecionando da galeria.
Clique em "Salvar Planta" para concluir.
Visualizar Detalhes da Planta:

Na lista principal, clique em uma planta para ver mais detalhes.
Você verá informações completas, foto e histórico de regas.
Registrar Rega:

Na tela de detalhes, clique no ícone de gota d'água para registrar uma nova rega.
O histórico será atualizado automaticamente.
Editar ou Excluir Planta:

Na tela de detalhes, use os ícones correspondentes para editar ou excluir a planta.
