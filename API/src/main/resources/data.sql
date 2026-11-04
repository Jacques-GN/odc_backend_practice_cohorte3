-- Ce fichier est utilisé par Spring Boot pour les profils de test (H2).
-- Les mots de passe ci-dessous sont déjà hachés en BCrypt (mot de passe en clair entre parenthèses) :
--   laurentgina@mail.com  -> laurent
--   sophiefoncek@mail.com -> sophie
--   agathefeeling@mail.com -> agathe

DELETE FROM employees;

INSERT INTO employees (first_name, last_name, mail, password) VALUES
  ('Laurent', 'GINA', 'laurentgina@mail.com', '$2b$10$qeFcvxAHEooT4hylqyVc9.uqANHqawFF0ssTZmdfdCS0CoQN3JzLS'),
  ('Sophie', 'FONCEK', 'sophiefoncek@mail.com', '$2b$10$L9CCin7Woa/8TIU1Fj9TBOsqqti4ZFf8q6dY4KKXxWgE2BVngK6mq'),
  ('Agathe', 'FEELING', 'agathefeeling@mail.com', '$2b$10$yW5/SvEiJS3Qn612Z856/ODnuGFuUB.thtuue6JN2n1UbmqWWPC6W');
