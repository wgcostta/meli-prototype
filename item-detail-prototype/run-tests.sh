#!/bin/bash

# Script para executar testes de forma simples

echo "🧪 MercadoClone - Test Runner"
echo "=============================="

# Verificar se as dependências estão instaladas
if [ ! -d "node_modules" ]; then
    echo "📦 Instalando dependências..."
    npm install
fi

# Verificar se jest.config.ts existe e renomear para .js se necessário
if [ -f "jest.config.ts" ]; then
    echo "🔧 Renomeando jest.config.ts para jest.config.js..."
    mv jest.config.ts jest.config.js
fi

# Verificar se jest.setup.ts existe e renomear para .js se necessário  
if [ -f "jest.setup.ts" ]; then
    echo "🔧 Renomeando jest.setup.ts para jest.setup.js..."
    mv jest.setup.ts jest.setup.js
fi

# Executar comando baseado no parâmetro
case "$1" in
    "install")
        echo "📦 Instalando dependências de teste..."
        npm install --save-dev @testing-library/jest-dom@^6.1.4 @testing-library/react@^14.1.2 @testing-library/user-event@^14.5.1 @types/jest@^29.5.8 jest@^29.7.0 jest-environment-jsdom@^29.7.0
        ;;
    "coverage")
        echo "📊 Executando testes com cobertura..."
        npm run test:coverage
        ;;
    "watch")
        echo "👁️ Executando testes em watch mode..."
        npm run test:watch
        ;;
    "ci")
        echo "🚀 Executando suite completa para CI..."
        npm run test:ci
        ;;
    "clean")
        echo "🧹 Limpando cache e artifacts..."
        rm -rf coverage .next node_modules/.cache
        npm test -- --clearCache
        ;;
    *)
        echo "🧪 Executando todos os testes..."
        npm test
        ;;
esac

echo "✅ Finalizado!"