import React from 'react';

const FAQ = () => {
  return (
    <section id="faq" className="faq">
      <h2>FAQ</h2>
      <p>Common questions about how the recipe generation works, privacy of data, and future features.</p>
      {/* Add more specific FAQs here as needed */}
      <div className="faq-item">
        <h4>How does the AI generate recipes?</h4>
        <p>Our AI uses advanced machine learning models trained on a vast database of recipes. It considers your ingredients, preferences, and dietary needs to create unique and suitable recipes.</p>
      </div>
      <div className="faq-item">
        <h4>Is my data private?</h4>
        <p>Yes, we prioritize your privacy. Input data is used solely for recipe generation and is not shared or stored long-term without your consent.</p>
      </div>
    </section>
  );
};

export default FAQ;