int number, i = 1, factorial = 1, sigma = 0, primes;
cin >> number;
if (number > 2) {                           // 1
    for (int j = 3; j <= number; j++) {     // 2
        factorial = 1;                      // 3
        i = 1;
        for (int x = 0; x < j - 2; x++) {   // 4
            factorial = factorial * i;      // 5
            i++;
        }
        sigma = sigma + (factorial - j * (factorial / j));  // 6
    }
    primes = -1 + sigma;    // 7
    if (number == 3)        // 8
        cout << 2;          // 9
    else
        cout << primes;     // 10
} else
    cout << ”wrong number”; // 11