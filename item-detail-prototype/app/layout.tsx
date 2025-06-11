import type { Metadata } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: 'Mercado Livre Prototype',
  description: 'A prototype for an item detail page inspired by Mercado Libre',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="pt-BR">
      <head>
        <link
          href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap"
          rel="stylesheet"
        />
      </head>
      <body>{children}</body>
    </html>
  );
}