import React from 'react';

const HeroSection = () => {
  const scrollToTryItOut = () => {
    document.getElementById('try-it-out')?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <section id="hero" className="hero">
      <h1>Your Ingredients. Your Taste. Your AI Chef.</h1>
      <p>Experience the future of cooking with AI-powered recipes.</p>
      <button onClick={scrollToTryItOut} className="cta-button">Try RecipAI Now</button>
    </section>
  );
};

export default HeroSection;