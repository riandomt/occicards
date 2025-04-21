# OcciCards - Application d'Apprentissage Interactive

## Introduction

**Sabatech** est une agence digitale spécialisée dans le développement de solutions numériques sur mesure. Basée en région Occitanie, elle accompagne les entreprises et institutions publiques dans leur transformation numérique, en mettant l'accent sur la sécurité et la souveraineté des données.

## Problématiques

Les méthodes d'apprentissage traditionnelles, comme les fiches de révision, le bachotage et la relecture passive, présentent plusieurs inconvénients :

- **Fiches de révision** : Elles sont souvent statiques et ne s'adaptent pas au niveau de maîtrise de l'apprenant, ce qui peut rendre la révision inefficace.
- **Bachotage** : Cette méthode consiste à réviser intensivement juste avant un examen, ce qui ne favorise pas la rétention à long terme et peut entraîner une surcharge cognitive.
- **Relecture passive** : Relire simplement les notes ou le matériel d'apprentissage sans interaction active ne stimule pas suffisamment la mémoire.

Pour remédier à ces problèmes, des techniques comme la **répétition espacée** et le **Système Leitner** sont utilisées. Elles améliorent la rétention à long terme en espaçant les révisions et en se concentrant sur les éléments moins bien maîtrisés.

## Solutions Proposées

Sabatech propose **OcciCards**, une application d'apprentissage basée sur des flashcards interactives. Elle utilise les principes de la répétition espacée et du Système Leitner pour offrir une expérience d'apprentissage personnalisée et efficace.

OcciCards est disponible en deux versions :

- **Application lourde** : Développée pour une utilisation hors connexion sur ordinateur, compatible avec Windows, macOS et Linux.
- **Application légère** : Accessible via un navigateur web, permettant un accès rapide et flexible aux flashcards depuis n'importe quel appareil connecté.

## Fonctionnalités

- **Création et gestion des decks** : Les utilisateurs peuvent créer des decks de cartes, chacune contenant une question et une réponse. Ils peuvent ajouter, modifier ou supprimer des cartes facilement.
- **Système de révision** : Les utilisateurs peuvent réviser leurs cartes en s'auto-évaluant. Les cartes mal mémorisées sont revues plus fréquemment, tandis que celles bien maîtrisées le sont moins souvent.
- **Interface intuitive** : Une interface utilisateur conviviale permet de naviguer facilement entre les différentes fonctionnalités de l'application.

## Documentation Technique

La documentation technique est générée à l'aide de l'outil **Javadoc**. Elle offre une vue complète des fonctionnalités, classes, méthodes et attributs de l'application.

### Guide pour Afficher la Documentation

1. **Générer la Documentation** :
   - Assurez-vous que le projet est compilé avec Maven.
   - Exécutez la commande suivante dans le répertoire racine du projet :
     ```bash
     mvn javadoc\:javadoc
     ```

2. **Accéder à la Documentation** :
   - Une fois la documentation générée, ouvrez le fichier `index.html` situé dans le répertoire `/occicards/target/site/apidocs`.
   - Vous pouvez naviguer à travers les différentes sections pour comprendre la structure du projet et les fonctionnalités offertes.

## Conclusion

OcciCards offre une solution efficace pour un apprentissage interactif et personnalisé. En s'appuyant sur des techniques éprouvées comme la répétition espacée et le Système Leitner, elle aide les utilisateurs à améliorer leur rétention des connaissances. Développée par Sabatech, cette application démontre la capacité de l'agence à créer des outils numériques performants pour l'éducation.
