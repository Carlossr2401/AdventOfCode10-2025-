# Advent of Code 2025 - Day 10: Factory

## Descripción del Proyecto

Este proyecto resuelve el desafío del **Día 10 del Advent of Code 2025**. El problema se sitúa en una fábrica donde las máquinas necesitan ser configuradas para alcanzar ciertos niveles de voltaje ("joltage") presionando una serie de botones.

El objetivo principal es encontrar el **número mínimo de pulsaciones totales** de botones necesarias para alinear los contadores internos de cada máquina con sus requisitos de voltaje específicos.

Matemáticamente, esto se modela como un sistema de ecuaciones lineales donde buscamos soluciones enteras no negativas que minimicen la suma de las variables (pulsaciones).

## Estructura del Código y Modularidad

El código está organizado en el paquete `software.aoc.day10.b` y demuestra una alta modularidad, separando claramente las responsabilidades:

- **Main**: Punto de entrada que orquesta la ejecución, conectando la lectura de datos con el solucionador.
- **FileInstructionReader**: Módulo dedicado exclusivamente a la E/S (Entrada/Salida) y el _parsing_ del archivo de entrada.
- **Solver**: Contiene la lógica algorítmica pura. Utiliza una clase interna `LinearSystemSolver` para encapsular la complejidad de la resolución matemática (eliminación gaussiana y búsqueda de soluciones enteras).
- **Modelos de Dominio** (`Machine`, `Button`, `ListOfMachines`): Clases (probablemente `metrics` o POJOs inmutables) que representan los datos del problema sin contener lógica de negocio.

## Principios SOLID

El diseño del proyecto respeta los principios SOLID de la siguiente manera:

1.  **SRP (Single Responsibility Principle)**: Es el principio más evidente.
    - `FileInstructionReader` solo se preocupa de interpretar el texto.
    - `Solver` solo se preocupa de las matemáticas.
    - `Machine` solo guarda datos.
2.  **OCP (Open/Closed Principle)**: El solucionador está diseñado para trabajar con cualquier lista de máquinas (`ListOfMachines`), permitiendo añadir nuevos casos de prueba sin modificar la lógica de resolución.
3.  **LSP (Liskov Substitution Principle)**: No se utilizan herencias complejas que violen este principio.
4.  **ISP (Interface Segregation Principle)**: Las clases exponen métodos específicos y directos, evitando interfaces sobredimensionadas.
5.  **DIP (Dependency Inversion Principle)**: Aunque `Main` instancia las clases concretas (típico en scripts de ejecución), el `Solver` depende de estructuras de datos abstractas (`ListOfMachines`) y no de la fuente de los datos (no sabe si vienen de un archivo o de memoria).

## Patrones de Diseño Utilizados

1.  **Value Object / DTO**: Uso intensivo de `Records` (como `Machine`, `Button`) para crear objetos inmutables que transportan datos a través de las capas de la aplicación.
2.  **Factory Method (Simplificado)**: El método `createMachine` dentro de `FileInstructionReader` actúa como una factoría que encapsula la lógica de creación de objetos complejos a partir de cadenas de texto.
3.  **Strategy (Implícito)**: La lógica de resolución (`LinearSystemSolver`) está aislada. Si el día de mañana se requiere otro algoritmo (ej. Simplex), se podría sustituir fácilmente esta parte sin afectar al resto del sistema.

## Ejecución

El sistema lee el archivo de entrada desde `src/main/resources/input`, procesa cada máquina buscando la solución óptima mediante un enfoque de sistemas lineales y búsqueda en variables libres, e imprime la respuesta final en consola.
