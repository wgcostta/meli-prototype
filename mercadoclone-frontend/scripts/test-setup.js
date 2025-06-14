#!/usr/bin/env node

/**
 * Test setup and runner script
 * Provides various testing utilities and commands
 */

const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

const commands = {
  'install': installTestDeps,
  'coverage': runCoverageReport,
  'watch': runWatchMode,
  'ci': runCITests,
  'validate': validateCoverage,
  'clean': cleanTestArtifacts,
  'help': showHelp
};

function main() {
  const command = process.argv[2] || 'help';
  
  if (commands[command]) {
    commands[command]();
  } else {
    console.error(`Unknown command: ${command}`);
    showHelp();
    process.exit(1);
  }
}

function installTestDeps() {
  console.log('📦 Installing test dependencies...');
  
  const testDeps = [
    '@testing-library/jest-dom@^6.1.4',
    '@testing-library/react@^14.1.2',
    '@testing-library/user-event@^14.5.1',
    '@types/jest@^29.5.8',
    'jest@^29.7.0',
    'jest-environment-jsdom@^29.7.0',
    'ts-jest@^29.1.1'
  ];

  try {
    execSync(`npm install --save-dev ${testDeps.join(' ')}`, { stdio: 'inherit' });
    console.log('✅ Test dependencies installed successfully');
  } catch (error) {
    console.error('❌ Failed to install test dependencies');
    process.exit(1);
  }
}

function runCoverageReport() {
  console.log('📊 Running coverage report...');
  
  try {
    execSync('npm run test:coverage', { stdio: 'inherit' });
    
    const coveragePath = path.join(process.cwd(), 'coverage', 'lcov-report', 'index.html');
    if (fs.existsSync(coveragePath)) {
      console.log(`\n📈 Coverage report generated: ${coveragePath}`);
      console.log('Open this file in your browser to view detailed coverage');
    }
  } catch (error) {
    console.error('❌ Coverage report failed');
    process.exit(1);
  }
}

function runWatchMode() {
  console.log('👁️  Running tests in watch mode...');
  console.log('Press "a" to run all tests, "f" to run only failed tests');
  console.log('Press "q" to quit watch mode');
  
  try {
    execSync('npm run test:watch', { stdio: 'inherit' });
  } catch (error) {
    console.error('❌ Watch mode failed');
    process.exit(1);
  }
}

function runCITests() {
  console.log('🚀 Running CI test suite...');
  
  const steps = [
    { name: 'Type checking', command: 'npm run type-check' },
    { name: 'Linting', command: 'npm run lint' },
    { name: 'Unit tests', command: 'npm run test:unit' },
    { name: 'Integration tests', command: 'npm run test:integration' },
    { name: 'Coverage report', command: 'npm run test:coverage' }
  ];

  for (const step of steps) {
    console.log(`\n🔄 ${step.name}...`);
    try {
      execSync(step.command, { stdio: 'inherit' });
      console.log(`✅ ${step.name} passed`);
    } catch (error) {
      console.error(`❌ ${step.name} failed`);
      process.exit(1);
    }
  }
  
  console.log('\n🎉 All CI tests passed!');
}

function validateCoverage() {
  console.log('🔍 Validating coverage thresholds...');
  
  const coverageFile = path.join(process.cwd(), 'coverage', 'coverage-summary.json');
  
  if (!fs.existsSync(coverageFile)) {
    console.error('❌ Coverage file not found. Run tests with coverage first.');
    process.exit(1);
  }
  
  try {
    const coverage = JSON.parse(fs.readFileSync(coverageFile, 'utf8'));
    const total = coverage.total;
    
    const thresholds = {
      statements: 80,
      branches: 80,
      functions: 80,
      lines: 80
    };
    
    let allPassed = true;
    
    console.log('\n📊 Coverage Summary:');
    for (const [metric, threshold] of Object.entries(thresholds)) {
      const actual = total[metric].pct;
      const status = actual >= threshold ? '✅' : '❌';
      const color = actual >= threshold ? '\x1b[32m' : '\x1b[31m';
      
      console.log(`${status} ${metric}: ${color}${actual}%\x1b[0m (threshold: ${threshold}%)`);
      
      if (actual < threshold) {
        allPassed = false;
      }
    }
    
    if (allPassed) {
      console.log('\n🎉 All coverage thresholds met!');
    } else {
      console.log('\n❌ Some coverage thresholds not met');
      process.exit(1);
    }
    
  } catch (error) {
    console.error('❌ Failed to validate coverage');
    process.exit(1);
  }
}

function cleanTestArtifacts() {
  console.log('🧹 Cleaning test artifacts...');
  
  const pathsToClean = [
    'coverage',
    '.nyc_output',
    'junit.xml',
    'test-results.xml'
  ];
  
  for (const p of pathsToClean) {
    const fullPath = path.join(process.cwd(), p);
    if (fs.existsSync(fullPath)) {
      try {
        if (fs.statSync(fullPath).isDirectory()) {
          fs.rmSync(fullPath, { recursive: true, force: true });
        } else {
          fs.unlinkSync(fullPath);
        }
        console.log(`✅ Removed ${p}`);
      } catch (error) {
        console.warn(`⚠️  Could not remove ${p}: ${error.message}`);
      }
    }
  }
  
  console.log('🎉 Cleanup complete');
}

function showHelp() {
  console.log(`
🧪 Test Setup and Runner Script

Usage: node scripts/test-setup.js <command>

Commands:
  install    Install all test dependencies
  coverage   Run tests with coverage report
  watch      Run tests in watch mode
  ci         Run full CI test suite
  validate   Validate coverage thresholds
  clean      Clean test artifacts
  help       Show this help message

Examples:
  node scripts/test-setup.js install
  node scripts/test-setup.js coverage
  node scripts/test-setup.js ci

Test Scripts:
  npm test              # Run all tests
  npm run test:watch    # Watch mode
  npm run test:coverage # With coverage
  npm run test:ci       # CI mode
  npm run test:unit     # Unit tests only
  npm run test:integration # Integration tests only
`);
}

if (require.main === module) {
  main();
}

module.exports = {
  installTestDeps,
  runCoverageReport,
  runWatchMode,
  runCITests,
  validateCoverage,
  cleanTestArtifacts
};