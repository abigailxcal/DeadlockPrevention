# Deadlock Prevention Algorithm 
## Role Summary
This implementation handles deadlock prevention using the resource ordering strategy.
It enforces a strict global ordering of resource acquisition to break the circular wait condition, one of the four necessary conditions for deadlock.

In short:
Processes always request resources in ascending ID order.
If they can’t get all the resources they need in that order, they release everything and retry later.

## How It Works
#### Deadlock Condition Prevented: Circular Wait
- By requiring resources to be acquired in increasing ID order, it’s impossible for a process to wait on another in a cycle.

#### Workflow
- Each process defines the resources it wants (e.g., Resource 1 and 3).

- It tries to lock them in sorted order.

- If even one is unavailable, it releases all and retries after a short delay.

- When successful, it simulates doing work, then releases the resources.



