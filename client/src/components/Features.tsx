import React from 'react';

const Features = () => {
  return (
    <section id="features" className="features">
      <h2>Features</h2>
      <ul>
        <li>Smart ingredient matching</li>
        <li>Custom dietary filters (vegan, gluten-free, keto, etc.)</li>
        <li>Cuisine style selection (e.g., Italian, Thai, Mediterranean)</li>
        <li>Save and share recipes <span className="coming-soon">(Coming Soon!)</span></li>
      </ul>
    </section>
  );
};

export default Features;