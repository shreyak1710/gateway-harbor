
const Index = () => {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-4">
      <div className="max-w-2xl w-full bg-white rounded-lg shadow-md p-8">
        <h1 className="text-3xl font-bold text-center mb-6">Spring Cloud Gateway Microservices</h1>
        <p className="text-center mb-8 text-gray-600">
          This project implements a microservices architecture with Spring Cloud Gateway.
        </p>
        
        <div className="space-y-4">
          <div className="border-l-4 border-blue-500 pl-4">
            <h2 className="text-xl font-semibold mb-2">Gateway Service</h2>
            <p className="text-gray-700">
              Handles routing, rate limiting, circuit breaking, and security.
            </p>
          </div>
          
          <div className="border-l-4 border-green-500 pl-4">
            <h2 className="text-xl font-semibold mb-2">Authentication Service</h2>
            <p className="text-gray-700">
              Manages user authentication and JWT token generation.
            </p>
          </div>
          
          <div className="border-l-4 border-purple-500 pl-4">
            <h2 className="text-xl font-semibold mb-2">Customer Service</h2>
            <p className="text-gray-700">
              Provides CRUD operations for customer data.
            </p>
          </div>
        </div>
        
        <div className="mt-8 text-center">
          <p className="text-sm text-gray-500">
            Built with Spring Boot 3.0.4, Java 21, and MongoDB
          </p>
        </div>
      </div>
    </div>
  );
};

export default Index;
