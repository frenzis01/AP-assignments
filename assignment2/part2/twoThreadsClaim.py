import functools
import time
import threading
import statistics as stats

def bench (n_threads:int=1,seq_iter:int=1,iter:int=1):
   exec_times = []
   def decorator(func):
      @functools.wraps(func)
      def wrapper(*args,**kwargs):
         def seq_iter_loop (func,*args, **kwargs): 
            for _ in range (seq_iter):
               func(*args,**kwargs)
         for _ in range(iter):
            workers = []
            start = time.perf_counter()
            print("Deploying threads: " + str(start))
            for i in range(n_threads):
               t_args = (func,) + args
               workers.append(threading.Thread(target=seq_iter_loop,args=t_args,kwargs=kwargs))
               workers[i].start()

            for t in workers:
               t.join()
            

            end = time.perf_counter()
            exec_times.append(end - start)

         mean = stats.mean(exec_times)
         variance = stats.variance(exec_times)

         return {
            'fun': func.__name__,
            'args': args,
            'n_threads': n_threads,
            'seq_iter': seq_iter,
            'iter': iter,
            'mean': mean,
            'variance': variance,
         }
      return wrapper
   return decorator
   
@bench(n_threads=2, seq_iter=8, iter=5)
def fib_run(n):
   a, b = 0, 1
   for _ in range(n):
      a, b = b, a + b
   return a

# When decorating with bench this, an infinite loop of thread creation starts 
def fib_run_recursive(n):
    if n <= 1:
        return n
    else:
        return fib_run_recursive(n-1) + fib_run_recursive(n-2)

result = fib_run(28)
print(result)