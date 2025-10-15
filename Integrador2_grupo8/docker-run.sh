#!/bin/bash

echo "========================================"
echo "  Integrador 2 - Sistema de Estudiantes"
echo "  Ejecutando con Docker"
echo "========================================"

echo ""
echo "Construyendo y ejecutando contenedores..."
docker-compose up --build

echo ""
echo "Aplicacion disponible en: http://localhost:8080"
echo "Base de datos MySQL en: localhost:3306"
echo ""
echo "Para detener: docker-compose down"
echo "Para ver logs: docker-compose logs -f"
echo "Para reiniciar: docker-compose restart"


